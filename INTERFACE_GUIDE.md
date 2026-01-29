# 🎨 Guide Visuel de l'Interface

## 📱 Interface Client JavaFX

### Vue d'Ensemble
```
┌─────────────────────────────────────────────────────────────┐
│  Logiciel de discussion en ligne - Alice              [_][□][X]│
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  Connecté à la salle de discussion.                        │
│  *** Bob a rejoint la discussion ***                       │
│  [10:30:15] Alice: Bonjour à tous !                        │
│  [10:30:20] Bob: Salut Alice !                            │
│  [10:30:25] Alice: Comment ça va ?                         │
│  [10:30:30] Bob: Très bien merci !                         │
│                                                             │
│                        (Zone de Messages)                   │
│                     Lecture seule, défilement               │
│                                                             │
│                                                             │
├─────────────────────────────────────────────────────────────┤
│ Tapez votre message ici...                    │ [Envoyer] │
└─────────────────────────────────────────────────────────────┘
```

### Composants de l'Interface

#### 1. Barre de Titre
```
┌────────────────────────────────────────┐
│ Logiciel de discussion en ligne - Alice│
└────────────────────────────────────────┘
```
- **Titre** : "Logiciel de discussion en ligne"
- **Pseudo** : Affiché après le tiret
- **Boutons** : Minimiser, Maximiser, Fermer

#### 2. Zone de Messages (TextArea)
```
┌──────────────────────────────────────────┐
│ Connecté à la salle de discussion.       │
│ *** Bob a rejoint la discussion ***      │
│ [10:30:15] Alice: Bonjour !              │
│ [10:30:20] Bob: Salut !                  │
│                                          │
│ ↕ Défilement automatique                 │
└──────────────────────────────────────────┘
```
- **Couleur de fond** : Gris clair (#DCDCDC)
- **Lecture seule** : Impossible d'éditer
- **Retour à la ligne** : Automatique
- **Défilement** : Vertical automatique

#### 3. Barre de Saisie
```
┌─────────────────────────────────────┬──────────┐
│ Tapez votre message ici...          │ Envoyer  │
└─────────────────────────────────────┴──────────┘
```
- **Champ de texte** : TextField avec placeholder
- **Bouton** : "Envoyer" (couleur verte)
- **Raccourci** : Touche Entrée

---

## 🔔 Boîtes de Dialogue

### Dialogue de Connexion
```
┌────────────────────────────────────┐
│  Connexion à la salle de discussion│
├────────────────────────────────────┤
│                                    │
│  Entrez votre pseudo :             │
│  ┌──────────────────────────────┐  │
│  │ Alice                        │  │
│  └──────────────────────────────┘  │
│                                    │
│              [ OK ]  [Annuler]     │
└────────────────────────────────────┘
```

### Dialogue d'Erreur
```
┌────────────────────────────────────┐
│  Erreur de connexion          [X]  │
├────────────────────────────────────┤
│  ⚠️                                 │
│  Impossible de se connecter à      │
│  la salle de discussion.           │
│  Vérifiez que le serveur est       │
│  démarré.                          │
│                                    │
│  Erreur: Connection refused        │
│                                    │
│                [ OK ]              │
└────────────────────────────────────┘
```

---

## 🎨 Personnalisation de l'Interface

### Modifier les Couleurs

#### Couleur de Fond de la Zone de Messages
```java
// Dans ChatUserImpl.java, méthode createGUI()

// Actuel : Gris clair
txtOutput.setStyle("-fx-control-inner-background: #DCDCDC;");

// Exemples de personnalisation :
txtOutput.setStyle("-fx-control-inner-background: #F0F0F0;"); // Gris très clair
txtOutput.setStyle("-fx-control-inner-background: #E8F5E9;"); // Vert clair
txtOutput.setStyle("-fx-control-inner-background: #E3F2FD;"); // Bleu clair
txtOutput.setStyle("-fx-control-inner-background: #FFF3E0;"); // Orange clair
```

#### Couleur du Bouton
```java
// Modifier le style du bouton
btnSend.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

// Exemples :
btnSend.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;"); // Bleu
btnSend.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white;"); // Orange
btnSend.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;"); // Violet
```

### Modifier les Dimensions

#### Taille de la Fenêtre
```java
// Dans createGUI()
Scene scene = new Scene(root, 600, 400); // Largeur: 600, Hauteur: 400

// Fenêtre plus grande
Scene scene = new Scene(root, 800, 600);

// Fenêtre plus petite
Scene scene = new Scene(root, 500, 350);
```

#### Espacement
```java
// Espacement dans la barre de saisie
HBox southPanel = new HBox(10); // 10 pixels d'espacement

// Marges
southPanel.setPadding(new Insets(10)); // 10 pixels de marge
```

---

## 🖼️ États de l'Interface

### État 1 : Démarrage
```
┌─────────────────────────────────────┐
│ Connexion à la salle de discussion  │
├─────────────────────────────────────┤
│ Entrez votre pseudo :               │
│ [___________________________]       │
│                                     │
│              [ OK ]  [Annuler]      │
└─────────────────────────────────────┘
```

### État 2 : Connecté (Vide)
```
┌─────────────────────────────────────┐
│ Chat - Alice                   [X]  │
├─────────────────────────────────────┤
│ Connecté à la salle de discussion.  │
│                                     │
│                                     │
│                                     │
├─────────────────────────────────────┤
│ Message...          │ [Envoyer]     │
└─────────────────────────────────────┘
```

### État 3 : Conversation Active
```
┌─────────────────────────────────────┐
│ Chat - Alice                   [X]  │
├─────────────────────────────────────┤
│ *** Bob a rejoint ***               │
│ [10:30] Alice: Salut !              │
│ [10:31] Bob: Hello !                │
│ [10:32] Alice: Ça va ?              │
│ [10:33] Bob: Oui et toi ?           │
├─────────────────────────────────────┤
│ Très bien merci !   │ [Envoyer]     │
└─────────────────────────────────────┘
```

### État 4 : Déconnexion
```
┌─────────────────────────────────────┐
│ Chat - Alice                   [X]  │ ← Clic sur X
├─────────────────────────────────────┤
│ *** Bob a rejoint ***               │
│ [10:30] Alice: Salut !              │
│ *** Alice a quitté ***              │ ← Notification envoyée
│                                     │
├─────────────────────────────────────┤
│ [Déconnexion en cours...]           │
└─────────────────────────────────────┘
```

---

## 📐 Diagramme de Flux Utilisateur

```
┌─────────────┐
│   Démarrer  │
│  le Client  │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│ Entrer le Pseudo    │
│ ┌─────────────────┐ │
│ │ Alice          │ │
│ └─────────────────┘ │
│   [OK] [Annuler]    │
└──────┬──────────────┘
       │
       ├─── Annuler ──→ [Fermeture]
       │
       ▼ OK
┌─────────────────────┐
│ Connexion au Serveur│
│      RMI...         │
└──────┬──────────────┘
       │
       ├─── Échec ──→ [Message d'erreur] ──→ [Fermeture]
       │
       ▼ Succès
┌─────────────────────┐
│  Fenêtre de Chat    │
│  ┌───────────────┐  │
│  │ Messages...   │  │
│  └───────────────┘  │
│  [______] [Envoyer] │
└──────┬──────────────┘
       │
       ├─── Envoyer Message ──→ [Diffusion via RMI]
       │         ↓
       │    [Affichage chez tous]
       │
       ├─── Recevoir Message ──→ [Affichage local]
       │
       └─── Fermer Fenêtre ──→ [Déconnexion] ──→ [Fin]
```

---

## 🎭 Types de Messages Affichés

### 1. Message Système
```
Format : Texte simple
Exemple : "Connecté à la salle de discussion."
Couleur : Par défaut (noir)
```

### 2. Notification d'Arrivée
```
Format : *** Pseudo a rejoint la discussion ***
Exemple : "*** Bob a rejoint la discussion ***"
Couleur : Par défaut (noir)
```

### 3. Notification de Départ
```
Format : *** Pseudo a quitté la discussion ***
Exemple : "*** Alice a quitté la discussion ***"
Couleur : Par défaut (noir)
```

### 4. Message Utilisateur
```
Format : [HH:mm:ss] Pseudo: Message
Exemple : "[10:30:15] Alice: Bonjour à tous !"
Couleur : Par défaut (noir)
```

---

## 🎨 Thème Sombre (Exemple d'Extension)

Pour implémenter un thème sombre, ajoutez ce code :

```java
// Dans createGUI()

// Thème sombre
root.setStyle("-fx-background-color: #2b2b2b;");

txtOutput.setStyle(
    "-fx-control-inner-background: #3c3c3c; " +
    "-fx-text-fill: #f5f5f5;"
);

txtMessage.setStyle(
    "-fx-background-color: #4a4a4a; " +
    "-fx-text-fill: #f5f5f5;"
);

btnSend.setStyle(
    "-fx-background-color: #4CAF50; " +
    "-fx-text-fill: white;"
);
```

Résultat :
```
┌─────────────────────────────────────┐
│ Chat - Alice                   [X]  │ ← Fond sombre
├─────────────────────────────────────┤
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│
│░ [10:30] Alice: Message...        ░│ ← Texte clair
│░ [10:31] Bob: Réponse...          ░│    sur fond
│░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░│    sombre
├─────────────────────────────────────┤
│ Message...          │ [Envoyer]     │
└─────────────────────────────────────┘
```

---

## 💡 Conseils d'Ergonomie

### ✅ Bonnes Pratiques
- Zone de messages suffisamment grande
- Champ de saisie facile d'accès
- Bouton visible et accessible
- Raccourci clavier (Entrée) fonctionnel
- Défilement automatique vers le bas

### ⚠️ À Éviter
- Fenêtre trop petite (< 400x300)
- Couleurs trop vives
- Texte difficile à lire
- Boutons trop petits
- Manque de feedback visuel

---

## 🔧 Personnalisations Avancées

### Ajouter une Icône
```java
// Dans createGUI()
primaryStage.getIcons().add(
    new Image(getClass().getResourceAsStream("/icon.png"))
);
```

### Empêcher le Redimensionnement
```java
primaryStage.setResizable(false);
```

### Définir Taille Min/Max
```java
primaryStage.setMinWidth(400);
primaryStage.setMinHeight(300);
primaryStage.setMaxWidth(1200);
primaryStage.setMaxHeight(800);
```

### Centrer la Fenêtre
```java
primaryStage.centerOnScreen();
```

---

**Guide créé pour le projet ChatRoom RMI**  
*Version 1.0 - 29 janvier 2026*
