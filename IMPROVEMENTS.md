# Améliorations et Extensions Possibles

Ce document présente des améliorations et extensions possibles pour enrichir l'application ChatRoom RMI.

## 1. Liste des Utilisateurs Connectés

### Modification de ChatUserImpl.java

Ajoutez une `ListView` pour afficher les utilisateurs connectés :

```java
private ListView<String> userListView;

// Dans createGUI()
userListView = new ListView<>();
userListView.setPrefWidth(150);
BorderPane.setMargin(userListView, new Insets(10));
root.setRight(userListView);
```

### Modification de ChatRoom.java

Ajoutez une méthode pour récupérer la liste des utilisateurs :

```java
List<String> getConnectedUsers() throws RemoteException;
```

### Modification de ChatRoomImpl.java

Implémentez la méthode :

```java
@Override
public synchronized List<String> getConnectedUsers() throws RemoteException {
    return new ArrayList<>(users.keySet());
}
```

## 2. Messages Privés

### Modification de ChatRoom.java

```java
void sendPrivateMessage(String from, String to, String message) throws RemoteException;
```

### Modification de ChatRoomImpl.java

```java
@Override
public synchronized void sendPrivateMessage(String from, String to, String message) 
        throws RemoteException {
    ChatUser recipient = users.get(to);
    if (recipient == null) {
        throw new RemoteException("L'utilisateur '" + to + "' n'est pas connecté.");
    }
    
    String formattedMessage = "[" + getCurrentTime() + "] [PRIVÉ] " + from + " -> " + to + ": " + message;
    
    try {
        // Envoyer au destinataire
        recipient.displayMessage(formattedMessage);
        // Envoyer à l'expéditeur pour confirmation
        users.get(from).displayMessage(formattedMessage);
    } catch (RemoteException e) {
        throw new RemoteException("Erreur lors de l'envoi du message privé: " + e.getMessage());
    }
}
```

### Dans ChatUserImpl.java

Ajoutez un bouton pour les messages privés :

```java
Button btnPrivate = new Button("Message Privé");
btnPrivate.setOnAction(e -> sendPrivateMessage());

private void sendPrivateMessage() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Message Privé");
    dialog.setHeaderText("Envoyer un message privé");
    dialog.setContentText("Destinataire:");
    
    Optional<String> recipient = dialog.showAndWait();
    if (recipient.isPresent()) {
        TextInputDialog msgDialog = new TextInputDialog();
        msgDialog.setTitle("Message Privé");
        msgDialog.setHeaderText("Message pour " + recipient.get());
        msgDialog.setContentText("Message:");
        
        Optional<String> message = msgDialog.showAndWait();
        if (message.isPresent()) {
            try {
                chatRoom.sendPrivateMessage(pseudo, recipient.get(), message.get());
            } catch (RemoteException e) {
                showError("Erreur", e.getMessage());
            }
        }
    }
}
```

## 3. Émojis et Formatage

### Ajout d'une barre d'émojis

```java
// Dans createGUI()
HBox emojiBar = new HBox(5);
emojiBar.setPadding(new Insets(5));

String[] emojis = {"😀", "😂", "❤️", "👍", "🎉", "😢", "😎", "🔥"};
for (String emoji : emojis) {
    Button emojiBtn = new Button(emoji);
    emojiBtn.setOnAction(e -> txtMessage.appendText(emoji));
    emojiBar.getChildren().add(emojiBtn);
}

VBox leftPanel = new VBox(10);
leftPanel.getChildren().addAll(emojiBar, southPanel);
root.setBottom(leftPanel);
```

## 4. Historique des Messages

### Sauvegarde dans un fichier

```java
private void saveMessageHistory() {
    try (PrintWriter writer = new PrintWriter(new FileWriter("chat_history_" + pseudo + ".txt", true))) {
        writer.println(txtOutput.getText());
    } catch (IOException e) {
        System.err.println("Erreur lors de la sauvegarde: " + e.getMessage());
    }
}

// Appeler cette méthode lors de la déconnexion
```

## 5. Notifications Sonores

### Ajout d'un son pour les nouveaux messages

```java
import javafx.scene.media.AudioClip;

private AudioClip notificationSound;

// Dans le constructeur
try {
    notificationSound = new AudioClip(
        getClass().getResource("/sounds/notification.wav").toString()
    );
} catch (Exception e) {
    System.err.println("Impossible de charger le son de notification");
}

// Dans displayMessage()
@Override
public void displayMessage(String message) throws RemoteException {
    Platform.runLater(() -> {
        txtOutput.appendText(message + "\n");
        if (notificationSound != null) {
            notificationSound.play();
        }
    });
}
```

## 6. Salles de Discussion Multiples

### Modification de l'architecture

Créez une nouvelle interface `ChatRoomManager` :

```java
public interface ChatRoomManager extends Remote {
    List<String> listRooms() throws RemoteException;
    ChatRoom joinRoom(String roomName) throws RemoteException;
    void createRoom(String roomName, String password) throws RemoteException;
}
```

## 7. Authentification

### Ajout d'un système de login

```java
public interface AuthenticationService extends Remote {
    boolean authenticate(String username, String password) throws RemoteException;
    boolean register(String username, String password, String email) throws RemoteException;
}

public class AuthenticationServiceImpl extends UnicastRemoteObject 
        implements AuthenticationService {
    
    private Map<String, User> users = new HashMap<>();
    
    @Override
    public boolean authenticate(String username, String password) throws RemoteException {
        User user = users.get(username);
        return user != null && user.checkPassword(password);
    }
    
    // Implémentation avec hashage de mot de passe
    private String hashPassword(String password) {
        // Utiliser BCrypt ou similar
        return password; // Simplifié pour l'exemple
    }
}
```

## 8. Interface Graphique Améliorée

### Utilisation de FXML pour l'interface

Créez un fichier `chat.fxml` :

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>

<BorderPane xmlns="http://javafx.com/javafx"
            xmlns:fx="http://javafx.com/fxml"
            fx:controller="com.chat.client.ChatController"
            prefHeight="400.0" prefWidth="600.0">
    
    <center>
        <TextArea fx:id="txtOutput" editable="false" wrapText="true"
                  style="-fx-control-inner-background: #DCDCDC;"/>
    </center>
    
    <bottom>
        <HBox spacing="10" padding="10">
            <TextField fx:id="txtMessage" promptText="Tapez votre message..."
                      HBox.hgrow="ALWAYS"/>
            <Button fx:id="btnSend" text="Envoyer" defaultButton="true"/>
        </HBox>
    </bottom>
    
    <right>
        <VBox spacing="5" padding="10" prefWidth="150">
            <Label text="Utilisateurs connectés" style="-fx-font-weight: bold;"/>
            <ListView fx:id="userList" VBox.vgrow="ALWAYS"/>
        </VBox>
    </right>
</BorderPane>
```

## 9. Thèmes et Personnalisation

### Ajout d'un fichier CSS

Créez `styles.css` :

```css
.root {
    -fx-font-family: "Segoe UI", sans-serif;
    -fx-font-size: 14px;
}

.text-area {
    -fx-background-color: #f5f5f5;
    -fx-text-fill: #333;
}

.text-field {
    -fx-background-radius: 5px;
    -fx-border-radius: 5px;
}

.button {
    -fx-background-color: #4CAF50;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-background-radius: 5px;
}

.button:hover {
    -fx-background-color: #45a049;
}

/* Thème sombre */
.dark-theme {
    -fx-background-color: #2b2b2b;
}

.dark-theme .text-area {
    -fx-background-color: #3c3c3c;
    -fx-text-fill: #f5f5f5;
}
```

Appliquez le CSS :

```java
scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
```

## 10. Partage de Fichiers

### Ajout de transfert de fichiers

```java
public interface FileTransferService extends Remote {
    byte[] downloadFile(String filename) throws RemoteException;
    void uploadFile(String filename, byte[] content) throws RemoteException;
}
```

### Dans le client

```java
private void sendFile() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Sélectionner un fichier");
    File file = fileChooser.showOpenDialog(stage);
    
    if (file != null) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            fileTransferService.uploadFile(file.getName(), fileContent);
            chatRoom.postMessage(pseudo, "[FICHIER] " + file.getName());
        } catch (IOException e) {
            showError("Erreur", "Impossible de lire le fichier");
        }
    }
}
```

## 11. Chiffrement des Messages

### Utilisation de cryptographie

```java
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class MessageEncryption {
    private SecretKey key;
    private Cipher cipher;
    
    public MessageEncryption() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        key = keyGen.generateKey();
        cipher = Cipher.getInstance("AES");
    }
    
    public String encrypt(String message) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }
    
    public String decrypt(String encryptedMessage) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decrypted);
    }
}
```

## 12. Indicateur de Frappe

### Notification quand quelqu'un tape

```java
// Dans ChatRoom.java
void notifyTyping(String pseudo, boolean isTyping) throws RemoteException;

// Dans ChatUser.java
void showTypingIndicator(String pseudo, boolean isTyping) throws RemoteException;

// Dans le client, sur le TextField
txtMessage.textProperty().addListener((obs, oldVal, newVal) -> {
    try {
        chatRoom.notifyTyping(pseudo, !newVal.isEmpty());
    } catch (RemoteException e) {
        // Gérer l'erreur
    }
});
```

## Conclusion

Ces améliorations peuvent être implémentées progressivement pour enrichir l'application. Commencez par les fonctionnalités les plus simples (liste des utilisateurs, émojis) avant de passer aux plus complexes (authentification, chiffrement).

Chaque amélioration doit être testée individuellement avant d'être intégrée au projet principal.
