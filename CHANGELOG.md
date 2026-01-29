# 📝 Historique des Versions

## Version 1.1 - Liste des Utilisateurs Connectés (29 janvier 2026)

### 🆕 Nouvelles Fonctionnalités

#### Liste des Utilisateurs en Temps Réel
- ✨ **Panneau latéral droit** affichant tous les utilisateurs connectés
- 📊 **Compteur d'utilisateurs** en haut de la liste
- 🔄 **Mise à jour automatique** lors des connexions/déconnexions
- 🎨 **Interface agrandie** : Fenêtre passée de 600x400 à 800x500 pixels

### 🔧 Modifications Techniques

#### Interfaces RMI
- **ChatRoom.java** : Ajout de `getConnectedUsers()` → retourne `List<String>`
- **ChatUser.java** : Ajout de `updateUserList(List<String> users)` → callback pour mise à jour

#### Implémentation Serveur
- **ChatRoomImpl.java** :
  - Implémentation de `getConnectedUsers()`
  - Nouvelle méthode `notifyUserListUpdate()` pour notifier tous les clients
  - Appels automatiques dans `subscribe()` et `unsubscribe()`

#### Implémentation Client
- **ChatUserImpl.java** :
  - Ajout de `ListView<String> userListView` pour afficher la liste
  - Ajout de `ObservableList<String> userList` pour les données
  - Ajout de `Label userCountLabel` pour le compteur
  - Implémentation de `updateUserList()` avec `Platform.runLater()`
  - Panneau droit (`VBox`) avec titre et liste
  - Fenêtre redimensionnée à 800x500

### 📚 Documentation
- 🆕 **FEATURE_USER_LIST.md** : Guide complet de la fonctionnalité
  - Description technique détaillée
  - Schémas de l'interface avant/après
  - Flux de mise à jour
  - Exemples de personnalisation
  - Tests recommandés
  - Idées d'amélioration futures

### 🎨 Interface Utilisateur

#### Avant (v1.0)
```
[Zone de messages              ]
[Champ message   ] [Envoyer   ]
```

#### Après (v1.1)
```
[Zone de messages   ] [Utilisateurs (3)]
                      [Alice           ]
                      [Bob             ]
                      [Charlie         ]
[Champ message      ] [Envoyer        ]
```

### 📊 Statistiques
- **Lignes de code ajoutées** : ~150 lignes
- **Fichiers modifiés** : 4 fichiers Java
- **Nouveau document** : 1 fichier (FEATURE_USER_LIST.md)
- **Taille du package** : 42 KB (vs 34 KB en v1.0)

---

## Version 1.0 - Version Initiale (29 janvier 2026)

### ✨ Fonctionnalités Principales

#### Communication RMI
- 🔄 **Architecture client-serveur** avec Java RMI
- 📡 **Communication bidirectionnelle** entre clients et serveur
- 🔒 **Thread-safe** avec méthodes synchronized

#### Interface JavaFX
- 🎨 **Interface moderne** avec JavaFX
- 💬 **Zone de messages** en lecture seule avec défilement
- ⌨️ **Champ de saisie** avec support de la touche Entrée
- 🖱️ **Bouton Envoyer** avec style personnalisé

#### Gestion des Utilisateurs
- 👤 **Connexion** avec pseudo unique
- 🚪 **Déconnexion propre** avec notification
- 🔔 **Notifications** d'arrivée et de départ
- ⏰ **Horodatage** des messages (HH:mm:ss)

### 🏗️ Architecture

#### Interfaces RMI
- **ChatRoom.java** : Interface de la salle de discussion
  - `subscribe(ChatUser, String)` : Inscription
  - `unsubscribe(String)` : Désinscription
  - `postMessage(String, String)` : Envoi de message

- **ChatUser.java** : Interface du client
  - `displayMessage(String)` : Réception de message

#### Implémentations
- **ChatRoomImpl.java** : Serveur de chat
  - Gestion de la Map des utilisateurs
  - Diffusion des messages
  - Gestion des connexions/déconnexions

- **ChatUserImpl.java** : Client JavaFX
  - Interface graphique complète
  - Connexion RMI
  - Gestion des événements

- **ChatServer.java** : Serveur RMI
  - Création du registre RMI
  - Port 1099
  - Hébergement de ChatRoomImpl

### 📚 Documentation (9 fichiers)

#### Guides Principaux
- **START_HERE.md** : Point de départ du projet
- **README.md** : Documentation principale
- **QUICKSTART.md** : Démarrage rapide en 3 minutes

#### Configuration
- **INTELLIJ_SETUP.md** : Configuration IntelliJ IDEA
- **pom.xml** : Configuration Maven

#### Architecture
- **ARCHITECTURE.md** : Diagrammes et explications
- **INTERFACE_GUIDE.md** : Guide visuel de l'interface

#### Développement
- **IMPROVEMENTS.md** : Idées d'améliorations
- **VERIFICATION.md** : Checklist de vérification
- **INDEX.md** : Table des matières

### ⚙️ Configuration

#### Maven (pom.xml)
- **Java** : JDK 17
- **JavaFX** : 21.0.1
- **Maven Compiler** : 3.11.0
- **JavaFX Plugin** : 0.0.8
- **Maven Shade** : 3.5.1 (pour JARs exécutables)

#### Scripts
- `start-server.bat` / `.sh` : Lancer le serveur
- `start-client.bat` / `.sh` : Lancer le client

### 📊 Statistiques
- **Fichiers Java** : 5
- **Lignes de code** : ~640
- **Documentation** : 9 fichiers
- **Scripts** : 4 fichiers
- **Taille du package** : 34 KB

### 🎯 Objectifs Pédagogiques
- ✅ Comprendre Java RMI
- ✅ Créer une interface JavaFX
- ✅ Implémenter un système distribué
- ✅ Gérer la concurrence
- ✅ Documenter un projet

---

## 🔮 Versions Futures (Planifiées)

### Version 1.2 (À venir)
- [ ] Messages privés entre utilisateurs
- [ ] Émojis et formatage du texte
- [ ] Sons de notification

### Version 1.3 (À venir)
- [ ] Salles de discussion multiples
- [ ] Historique des messages persistant
- [ ] Avatars des utilisateurs

### Version 2.0 (À venir)
- [ ] Authentification sécurisée
- [ ] Chiffrement des messages
- [ ] Partage de fichiers

---

## 📋 Notes de Migration

### De v1.0 à v1.1

#### Changements Obligatoires
1. **Interfaces** : Toutes les implémentations doivent être mises à jour
   - `ChatRoom` : Ajouter `getConnectedUsers()`
   - `ChatUser` : Ajouter `updateUserList(List<String>)`

2. **Client** : L'interface est plus large (800x500 au lieu de 600x400)

#### Rétrocompatibilité
- ⚠️ **Pas de rétrocompatibilité** : Les clients v1.0 ne peuvent pas se connecter à un serveur v1.1
- 🔄 **Migration nécessaire** : Tous les composants doivent être mis à jour ensemble

#### Procédure de Mise à Jour
```bash
1. Arrêter tous les clients
2. Arrêter le serveur
3. Mettre à jour le code
4. Recompiler : mvn clean compile
5. Redémarrer le serveur
6. Lancer les nouveaux clients
```

---

## 🐛 Corrections de Bugs

### Version 1.1
- Aucun bug connu pour le moment

### Version 1.0
- Aucun bug connu pour le moment

---

## 🙏 Contributions

### Développeurs
- Projet développé pour le cours Web Services
- DIC3-M2GL&SI/DGI/ESP/UCAD/SN

### Remerciements
- Java RMI Documentation
- JavaFX Community
- Maven Project

---

**Dernière mise à jour** : 29 janvier 2026  
**Version actuelle** : 1.1  
**Status** : ✅ Stable et Testé
