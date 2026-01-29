# ChatRoom RMI avec JavaFX

Application de chat distribuée utilisant Java RMI et JavaFX, développée pour le cours Web Services.

## Description

Ce projet implémente un système de chat en ligne permettant à plusieurs utilisateurs de communiquer simultanément. L'architecture utilise Java RMI (Remote Method Invocation) pour la communication entre les clients et le serveur, avec une interface utilisateur moderne en JavaFX.

## Architecture

Le projet est composé de trois parties principales :

### 1. Interfaces RMI
- **ChatRoom** : Interface pour la salle de discussion (serveur)
  - `subscribe(ChatUser, String)` : Inscription d'un utilisateur
  - `unsubscribe(String)` : Désinscription d'un utilisateur
  - `postMessage(String, String)` : Publication d'un message

- **ChatUser** : Interface pour les clients
  - `displayMessage(String)` : Réception et affichage des messages

### 2. Implémentations
- **ChatRoomImpl** : Implémentation du serveur de chat
  - Gestion des utilisateurs connectés
  - Diffusion des messages à tous les clients
  - Notifications d'arrivée/départ

- **ChatUserImpl** : Implémentation du client avec JavaFX
  - Interface graphique moderne
  - Connexion au serveur RMI
  - Envoi et réception de messages

### 3. Serveur
- **ChatServer** : Serveur principal
  - Création du registre RMI
  - Hébergement de la salle de discussion

## Prérequis

- Java Development Kit (JDK) 17 ou supérieur
- Maven 3.6 ou supérieur
- IntelliJ IDEA (recommandé)

## Configuration dans IntelliJ IDEA

### 1. Importer le projet

1. Ouvrez IntelliJ IDEA
2. Sélectionnez `File > Open`
3. Choisissez le dossier `chatroom-rmi`
4. IntelliJ détectera automatiquement le projet Maven

### 2. Configuration du JDK

1. `File > Project Structure > Project`
2. Définir le SDK sur Java 17
3. Définir le niveau de langage sur 17

### 3. Synchroniser Maven

1. Clic droit sur `pom.xml`
2. Sélectionner `Maven > Reload Project`

## Compilation et Exécution

### Méthode 1 : Avec Maven (ligne de commande)

```bash
# Compiler le projet
mvn clean compile

# Démarrer le serveur (Terminal 1)
mvn exec:java -Dexec.mainClass="com.chat.server.ChatServer"

# Démarrer le client (Terminal 2, 3, etc.)
mvn javafx:run
```

### Méthode 2 : Avec IntelliJ IDEA

#### Démarrer le serveur

1. Clic droit sur `ChatServer.java`
2. Sélectionner `Run 'ChatServer.main()'`

Ou créer une configuration de lancement :
1. `Run > Edit Configurations > + > Application`
2. Nom : `ChatServer`
3. Main class : `com.chat.server.ChatServer`
4. Module : `chatroom-rmi`

#### Démarrer le client

1. Clic droit sur `ChatUserImpl.java`
2. Sélectionner `Run 'ChatUserImpl$ChatApplication.main()'`

Ou créer une configuration de lancement :
1. `Run > Edit Configurations > + > Application`
2. Nom : `ChatClient`
3. Main class : `com.chat.client.ChatUserImpl$ChatApplication`
4. Module : `chatroom-rmi`
5. VM options : `--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml`

### Méthode 3 : Créer des JARs exécutables

```bash
# Compiler et créer les JARs
mvn clean package

# Démarrer le serveur
java -jar target/chatroom-server.jar

# Démarrer le client
java -jar target/chatroom-client.jar
```

## Utilisation

### 1. Démarrer le serveur

Lancez d'abord le serveur. Vous verrez :
```
Démarrage du serveur de chat...
Serveur de chat démarré avec succès !
Port: 1099
Nom de liaison: ChatRoom
En attente de connexions...
```

### 2. Démarrer les clients

Lancez plusieurs instances du client. Pour chaque client :

1. Une boîte de dialogue apparaît pour saisir votre pseudo
2. Entrez un pseudo unique et cliquez sur OK
3. La fenêtre de chat s'ouvre
4. Tapez vos messages dans le champ en bas
5. Cliquez sur "Envoyer" ou appuyez sur Entrée

### 3. Fonctionnalités

- **Envoi de messages** : Tous les utilisateurs reçoivent instantanément vos messages
- **Notifications** : Les arrivées et départs sont notifiés à tous
- **Horodatage** : Chaque message est horodaté
- **Déconnexion** : Fermez simplement la fenêtre pour vous déconnecter

## Structure du projet

```
chatroom-rmi/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── chat/
                    ├── interfaces/
                    │   ├── ChatRoom.java
                    │   └── ChatUser.java
                    ├── impl/
                    │   └── ChatRoomImpl.java
                    ├── client/
                    │   └── ChatUserImpl.java
                    └── server/
                        └── ChatServer.java
```

## Différences avec l'implémentation Swing

Cette version JavaFX offre plusieurs avantages par rapport à Swing :

1. **Interface moderne** : Apparence plus actuelle et professionnelle
2. **Meilleure gestion des threads** : Utilisation de `Platform.runLater()`
3. **CSS styling** : Possibilité de personnaliser l'apparence
4. **Binding de données** : Meilleure intégration des données
5. **Animations** : Support natif pour les animations

## Dépannage

### Erreur : "Impossible de se connecter à la salle de discussion"

- Vérifiez que le serveur est démarré
- Vérifiez que le port 1099 n'est pas bloqué par un pare-feu
- Assurez-vous que l'hôte est correct (`localhost` par défaut)

### Erreur : "Le pseudo est déjà utilisé"

- Choisissez un pseudo différent
- Un seul pseudo par utilisateur

### Erreur JavaFX

Si vous rencontrez des erreurs liées à JavaFX :
```bash
# Ajouter les modules JavaFX aux VM options
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

## Améliorations possibles

- [ ] Ajout d'une liste des utilisateurs connectés
- [ ] Messages privés entre utilisateurs
- [ ] Historique des messages
- [ ] Émojis et formatage du texte
- [ ] Pièces jointes
- [ ] Salles de discussion multiples
- [ ] Authentification des utilisateurs
- [ ] Chiffrement des messages

## Auteur

Projet développé pour le cours Web Services - DIC3-M2GL&SI/DGI/ESP/UCAD/SN

## Licence

Ce projet est destiné à des fins éducatives.
