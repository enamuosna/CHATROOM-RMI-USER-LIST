# 👥 Fonctionnalité : Liste des Utilisateurs Connectés

## 📋 Description

Cette fonctionnalité ajoute une **liste en temps réel** des utilisateurs connectés à la salle de discussion. La liste est affichée dans un panneau latéral sur le côté droit de l'interface client.

## ✨ Caractéristiques

- ✅ **Mise à jour automatique** : La liste se rafraîchit automatiquement quand un utilisateur rejoint ou quitte
- ✅ **Compteur d'utilisateurs** : Affiche le nombre total d'utilisateurs connectés
- ✅ **Interface intuitive** : Panneau latéral clair et lisible
- ✅ **Synchronisation RMI** : Tous les clients voient la même liste en temps réel

## 🎨 Interface Utilisateur

### Avant (Sans Liste)
```
┌─────────────────────────────────────┐
│ Chat - Alice                   [X]  │
├─────────────────────────────────────┤
│ [10:30] Alice: Bonjour !            │
│ [10:31] Bob: Salut !                │
│                                     │
├─────────────────────────────────────┤
│ Message...          │ [Envoyer]     │
└─────────────────────────────────────┘
```

### Après (Avec Liste)
```
┌────────────────────────────────────────────────┐
│ Chat - Alice                              [X]  │
├──────────────────────────────┬─────────────────┤
│ [10:30] Alice: Bonjour !     │ Utilisateurs (3)│
│ [10:31] Bob: Salut !         │ ┌─────────────┐ │
│ [10:32] Charlie: Hello !     │ │ Alice       │ │
│                              │ │ Bob         │ │
│                              │ │ Charlie     │ │
│                              │ └─────────────┘ │
├──────────────────────────────┴─────────────────┤
│ Message...                   │ [Envoyer]       │
└────────────────────────────────────────────────┘
```

## 🔧 Modifications Techniques

### 1. Interface ChatRoom (ChatRoom.java)

**Ajout d'une nouvelle méthode :**
```java
/**
 * Récupère la liste des utilisateurs actuellement connectés.
 * 
 * @return Liste des pseudonymes des utilisateurs connectés
 * @throws RemoteException En cas d'erreur de communication RMI
 */
List<String> getConnectedUsers() throws RemoteException;
```

### 2. Interface ChatUser (ChatUser.java)

**Ajout d'une méthode de callback :**
```java
/**
 * Met à jour la liste des utilisateurs connectés.
 * 
 * @param users Liste des pseudonymes des utilisateurs connectés
 * @throws RemoteException En cas d'erreur de communication RMI
 */
void updateUserList(List<String> users) throws RemoteException;
```

### 3. Implémentation Serveur (ChatRoomImpl.java)

**Implémentation de getConnectedUsers :**
```java
@Override
public synchronized List<String> getConnectedUsers() throws RemoteException {
    return new ArrayList<>(users.keySet());
}
```

**Nouvelle méthode notifyUserListUpdate :**
```java
/**
 * Notifie tous les utilisateurs de la mise à jour de la liste des connectés.
 */
private void notifyUserListUpdate() {
    Map<String, ChatUser> usersCopy = new HashMap<>(users);
    List<String> connectedUsers = new ArrayList<>(users.keySet());
    
    for (Map.Entry<String, ChatUser> entry : usersCopy.entrySet()) {
        try {
            entry.getValue().updateUserList(connectedUsers);
        } catch (RemoteException e) {
            System.err.println("Erreur lors de la mise à jour de la liste pour " 
                + entry.getKey() + ": " + e.getMessage());
            users.remove(entry.getKey());
        }
    }
}
```

**Appels dans subscribe et unsubscribe :**
```java
@Override
public synchronized void subscribe(ChatUser user, String pseudo) throws RemoteException {
    // ... code existant ...
    
    // Notifier tous les utilisateurs de la mise à jour de la liste
    notifyUserListUpdate();
}

@Override
public synchronized void unsubscribe(String pseudo) throws RemoteException {
    // ... code existant ...
    
    // Notifier tous les utilisateurs de la mise à jour de la liste
    notifyUserListUpdate();
}
```

### 4. Implémentation Client (ChatUserImpl.java)

**Ajout de nouveaux composants JavaFX :**
```java
private ListView<String> userListView;
private ObservableList<String> userList;
private Label userCountLabel;

public ChatUserImpl() throws RemoteException {
    super();
    this.userList = FXCollections.observableArrayList();
}
```

**Création de l'interface avec la liste :**
```java
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

// Ajouter le panneau à droite
root.setRight(rightPanel);

// Agrandir la fenêtre pour accommoder la liste
Scene scene = new Scene(root, 800, 500);
```

**Implémentation de updateUserList :**
```java
@Override
public void updateUserList(List<String> users) throws RemoteException {
    Platform.runLater(() -> {
        userList.clear();
        userList.addAll(users);
        userCountLabel.setText("Utilisateurs (" + users.size() + ")");
    });
}
```

## 🔄 Flux de Mise à Jour

### Quand un Utilisateur Rejoint

```
1. Alice se connecte
   ↓
2. ChatRoomImpl.subscribe()
   ↓
3. Ajoute Alice à la Map<String, ChatUser>
   ↓
4. Diffuse "*** Alice a rejoint ***"
   ↓
5. Appelle notifyUserListUpdate()
   ↓
6. Récupère la liste actuelle : [Alice, Bob, Charlie]
   ↓
7. Pour chaque client :
   - Appelle client.updateUserList([Alice, Bob, Charlie])
   ↓
8. Chaque client met à jour sa ListView JavaFX
```

### Quand un Utilisateur Quitte

```
1. Bob se déconnecte
   ↓
2. ChatRoomImpl.unsubscribe("Bob")
   ↓
3. Supprime Bob de la Map
   ↓
4. Diffuse "*** Bob a quitté ***"
   ↓
5. Appelle notifyUserListUpdate()
   ↓
6. Récupère la liste actuelle : [Alice, Charlie]
   ↓
7. Pour chaque client :
   - Appelle client.updateUserList([Alice, Charlie])
   ↓
8. Chaque client met à jour sa ListView JavaFX
```

## 🎯 Avantages

### Pour l'Utilisateur
- 👀 **Visibilité** : Voir qui est en ligne instantanément
- 📊 **Compteur** : Savoir combien d'utilisateurs sont connectés
- 🔄 **Temps réel** : Mise à jour automatique sans action requise

### Pour le Développeur
- 🏗️ **Architecture propre** : Utilise le pattern Observer via RMI
- 🔒 **Thread-safe** : Méthodes synchronized
- 🎨 **JavaFX réactif** : ObservableList intégré

## 📐 Dimensions de l'Interface

### Avant
- **Largeur** : 600px
- **Hauteur** : 400px

### Après
- **Largeur** : 800px (+200px pour la liste)
- **Hauteur** : 500px (+100px pour plus de confort)
- **Liste** : 180px de large

## 🎨 Personnalisation

### Changer la Largeur de la Liste
```java
userListView.setPrefWidth(200); // Au lieu de 180
```

### Changer le Style de la Liste
```java
// Fond blanc
userListView.setStyle("-fx-background-color: white;");

// Bordure
userListView.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1px;");
```

### Changer la Police du Label
```java
userCountLabel.setStyle(
    "-fx-font-weight: bold; " +
    "-fx-font-size: 16px; " +
    "-fx-text-fill: #333333;"
);
```

### Ajouter des Icônes aux Utilisateurs
```java
userListView.setCellFactory(lv -> new ListCell<String>() {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            // Ajouter une icône utilisateur
            Label icon = new Label("👤");
            setGraphic(icon);
        }
    }
});
```

## 🧪 Tests

### Test 1 : Connexion d'un Utilisateur
```
1. Lancer le serveur
2. Lancer le client 1 (Alice)
3. Vérifier : Liste affiche "Utilisateurs (1)" avec "Alice"
```

### Test 2 : Connexion Multiple
```
1. Lancer le client 2 (Bob)
2. Vérifier dans Alice : Liste affiche "Utilisateurs (2)" avec "Alice" et "Bob"
3. Vérifier dans Bob : Liste affiche "Utilisateurs (2)" avec "Alice" et "Bob"
```

### Test 3 : Déconnexion
```
1. Fermer le client Bob
2. Vérifier dans Alice : Liste affiche "Utilisateurs (1)" avec seulement "Alice"
```

### Test 4 : Ordre Alphabétique (Optionnel)
```java
// Pour trier la liste par ordre alphabétique
@Override
public void updateUserList(List<String> users) throws RemoteException {
    Platform.runLater(() -> {
        userList.clear();
        users.sort(String::compareToIgnoreCase);
        userList.addAll(users);
        userCountLabel.setText("Utilisateurs (" + users.size() + ")");
    });
}
```

## 🚀 Améliorations Futures

### 1. Indicateur de Statut
```java
// Ajouter un indicateur vert pour l'utilisateur actif
if (item.equals(pseudo)) {
    setText("🟢 " + item + " (vous)");
} else {
    setText(item);
}
```

### 2. Clic sur Utilisateur pour Message Privé
```java
userListView.setOnMouseClicked(event -> {
    if (event.getClickCount() == 2) {
        String selectedUser = userListView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && !selectedUser.equals(pseudo)) {
            // Ouvrir dialogue pour message privé
            sendPrivateMessage(selectedUser);
        }
    }
});
```

### 3. Menu Contextuel
```java
ContextMenu contextMenu = new ContextMenu();
MenuItem privateMsg = new MenuItem("Envoyer un message privé");
privateMsg.setOnAction(e -> {
    String selected = userListView.getSelectionModel().getSelectedItem();
    if (selected != null) sendPrivateMessage(selected);
});
contextMenu.getItems().add(privateMsg);
userListView.setContextMenu(contextMenu);
```

## 📝 Notes Importantes

### Synchronisation
- Les méthodes `subscribe`, `unsubscribe`, et `getConnectedUsers` sont **synchronized**
- Utilisation de `Platform.runLater()` pour mettre à jour l'UI JavaFX

### Gestion d'Erreurs
- Si un client est déconnecté de force, il est automatiquement retiré de la liste
- Les autres clients sont notifiés de la mise à jour

### Performance
- La liste est copiée (`new ArrayList<>`) pour éviter les problèmes de concurrence
- Les mises à jour sont groupées (une seule notification par changement)

## 🎓 Points d'Apprentissage

Cette fonctionnalité illustre :
- ✅ **Pattern Observer** : Le serveur notifie tous les clients
- ✅ **RMI Bidirectionnel** : Le serveur appelle les clients
- ✅ **JavaFX ObservableList** : Liaison automatique ListView ↔ Données
- ✅ **Thread Safety** : Synchronisation et Platform.runLater()
- ✅ **Architecture Distribuée** : Cohérence entre tous les clients

---

**Version :** 1.1 avec Liste des Utilisateurs  
**Date :** 29 janvier 2026  
**Status :** ✅ Fonctionnel et Testé
