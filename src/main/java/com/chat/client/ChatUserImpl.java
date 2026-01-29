package com.chat.client;

import com.chat.interfaces.ChatRoom;
import com.chat.interfaces.ChatUser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du client de chat avec JavaFX.
 * Permet à un utilisateur de se connecter à une salle de discussion RMI.
 */
public class ChatUserImpl extends UnicastRemoteObject implements ChatUser {
    
    private static final long serialVersionUID = 1L;
    private static final String TITLE = "Logiciel de discussion en ligne";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;
    
    private String pseudo;
    private ChatRoom chatRoom;
    private TextArea txtOutput;
    private TextField txtMessage;
    private Button btnSend;
    private ListView<String> userListView;
    private ObservableList<String> userList;
    private Label userCountLabel;
    private Stage stage;
    
    public ChatUserImpl() throws RemoteException {
        super();
        this.userList = FXCollections.observableArrayList();
    }
    
    /**
     * Initialise l'interface graphique.
     */
    public void createGUI(Stage primaryStage) {
        this.stage = primaryStage;
        
        // Zone de texte pour afficher les messages (lecture seule)
        txtOutput = new TextArea();
        txtOutput.setEditable(false);
        txtOutput.setWrapText(true);
        txtOutput.setStyle("-fx-control-inner-background: #DCDCDC;");
        
        // Liste des utilisateurs connectés
        userListView = new ListView<>(userList);
        userListView.setPrefWidth(180);
        userListView.setStyle("-fx-background-color: #F5F5F5;");
        
        // Label pour le titre de la liste
        userCountLabel = new Label("Utilisateurs (0)");
        userCountLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Panneau droit avec la liste des utilisateurs
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.getChildren().addAll(userCountLabel, userListView);
        VBox.setVgrow(userListView, Priority.ALWAYS);
        
        // Champ de saisie pour le message
        txtMessage = new TextField();
        txtMessage.setPromptText("Tapez votre message ici...");
        HBox.setHgrow(txtMessage, Priority.ALWAYS);
        
        // Bouton d'envoi
        btnSend = new Button("Envoyer");
        btnSend.setDefaultButton(true);
        btnSend.setOnAction(e -> sendMessage());
        
        // Gestion de la touche Entrée dans le champ de texte
        txtMessage.setOnAction(e -> sendMessage());
        
        // Panneau du bas contenant le champ de saisie et le bouton
        HBox southPanel = new HBox(10);
        southPanel.setPadding(new Insets(10));
        southPanel.getChildren().addAll(txtMessage, btnSend);
        
        // Panneau principal
        BorderPane root = new BorderPane();
        root.setCenter(txtOutput);
        root.setRight(rightPanel);
        root.setBottom(southPanel);
        
        // Configuration de la scène
        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle(TITLE + " - " + pseudo);
        primaryStage.setScene(scene);
        
        // Gestion de la fermeture de la fenêtre
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            disconnect();
        });
        
        primaryStage.show();
        txtMessage.requestFocus();
    }
    
    /**
     * Demande le pseudo à l'utilisateur.
     */
    public boolean requestPseudo() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(TITLE);
        dialog.setHeaderText("Connexion à la salle de discussion");
        dialog.setContentText("Entrez votre pseudo :");
        
        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            this.pseudo = result.get().trim();
            return true;
        }
        
        return false;
    }
    
    /**
     * Se connecte à la salle de discussion RMI.
     */
    public boolean connectToChatRoom(String host, int port) {
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            chatRoom = (ChatRoom) registry.lookup("ChatRoom");
            chatRoom.subscribe(this, pseudo);
            
            Platform.runLater(() -> 
                txtOutput.appendText("Connecté à la salle de discussion.\n")
            );
            
            return true;
            
        } catch (Exception e) {
            showError("Erreur de connexion", 
                     "Impossible de se connecter à la salle de discussion.\n" +
                     "Vérifiez que le serveur est démarré.\n\n" +
                     "Erreur: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Envoie un message à la salle de discussion.
     */
    private void sendMessage() {
        String message = txtMessage.getText().trim();
        
        if (!message.isEmpty()) {
            try {
                chatRoom.postMessage(pseudo, message);
                txtMessage.clear();
                txtMessage.requestFocus();
                
            } catch (RemoteException e) {
                showError("Erreur d'envoi", 
                         "Impossible d'envoyer le message.\n\n" +
                         "Erreur: " + e.getMessage());
            }
        }
    }
    
    /**
     * Se déconnecte de la salle de discussion.
     */
    private void disconnect() {
        try {
            if (chatRoom != null) {
                chatRoom.unsubscribe(pseudo);
            }
            
            // Unexport l'objet RMI
            UnicastRemoteObject.unexportObject(this, true);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la déconnexion: " + e.getMessage());
        } finally {
            Platform.exit();
            System.exit(0);
        }
    }
    
    @Override
    public void displayMessage(String message) throws RemoteException {
        Platform.runLater(() -> txtOutput.appendText(message + "\n"));
    }
    
    @Override
    public void updateUserList(List<String> users) throws RemoteException {
        Platform.runLater(() -> {
            userList.clear();
            userList.addAll(users);
            userCountLabel.setText("Utilisateurs (" + users.size() + ")");
        });
    }
    
    /**
     * Affiche une boîte de dialogue d'erreur.
     */
    private void showError(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
    
    /**
     * Classe principale pour lancer l'application JavaFX.
     */
    public static class ChatApplication extends Application {
        
        @Override
        public void start(Stage primaryStage) {
            try {
                ChatUserImpl client = new ChatUserImpl();
                
                // Demander le pseudo
                if (!client.requestPseudo()) {
                    Platform.exit();
                    return;
                }
                
                // Créer l'interface graphique
                client.createGUI(primaryStage);
                
                // Se connecter à la salle de discussion
                if (!client.connectToChatRoom(DEFAULT_HOST, DEFAULT_PORT)) {
                    Platform.exit();
                }
                
            } catch (RemoteException e) {
                System.err.println("Erreur lors de la création du client: " + e.getMessage());
                e.printStackTrace();
                Platform.exit();
            }
        }
        
        public static void main(String[] args) {
            launch(args);
        }
    }
}
