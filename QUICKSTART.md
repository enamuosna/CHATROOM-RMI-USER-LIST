# 🚀 Guide de Démarrage Rapide

## ⚡ Lancement en 3 Minutes

### Étape 1 : Prérequis (1 min)
```bash
# Vérifier Java 17
java -version
# Doit afficher : java version "17" ou supérieur

# Vérifier Maven
mvn -version
# Doit afficher : Apache Maven 3.6 ou supérieur
```

**Si Java ou Maven manquent :**
- Java 17 : https://adoptium.net/
- Maven : https://maven.apache.org/download.cgi

### Étape 2 : Compiler le Projet (1 min)
```bash
# Aller dans le dossier du projet
cd chatroom-rmi

# Compiler avec Maven
mvn clean compile
```

### Étape 3 : Lancer l'Application (1 min)

**Terminal 1 - Démarrer le Serveur :**
```bash
mvn exec:java -Dexec.mainClass="com.chat.server.ChatServer"
```

Attendez de voir :
```
✅ Serveur de chat démarré avec succès !
   Port: 1099
```

**Terminal 2 - Démarrer le Premier Client :**
```bash
mvn javafx:run
```

1. Entrez votre pseudo : `Alice`
2. Cliquez OK
3. La fenêtre de chat s'ouvre !

**Terminal 3 - Démarrer un Deuxième Client :**
```bash
mvn javafx:run
```

1. Entrez votre pseudo : `Bob`
2. Cliquez OK
3. Chattez avec Alice !

---

## 🎯 Utilisation avec IntelliJ IDEA

### Import Express (2 min)

1. **Ouvrir IntelliJ IDEA**
2. `File > Open` → Sélectionner le dossier `chatroom-rmi`
3. Attendre la synchronisation Maven (barre de progression en bas)

### Lancement Express

1. **Ouvrir** `ChatServer.java`
2. **Clic droit** sur le fichier → `Run 'ChatServer.main()'`
3. ✅ Le serveur démarre

4. **Ouvrir** `ChatUserImpl.java`
5. **Clic droit** sur le fichier → `Run 'ChatUserImpl$ChatApplication.main()'`
6. ✅ Le client démarre

7. **Répéter** l'étape 5 pour lancer plus de clients

---

## 💬 Premiers Pas dans le Chat

### Envoyer un Message
1. Tapez votre message dans le champ en bas
2. Appuyez sur **Entrée** ou cliquez sur **Envoyer**
3. Tous les utilisateurs connectés reçoivent le message !

### Ce que Vous Verrez
```
Connecté à la salle de discussion.
*** Alice a rejoint la discussion ***
*** Bob a rejoint la discussion ***
[10:30:15] Alice: Bonjour à tous !
[10:30:20] Bob: Salut Alice !
```

### Quitter
Fermez simplement la fenêtre. Les autres verront :
```
*** Alice a quitté la discussion ***
```

---

## 🔧 Scripts de Démarrage Rapide

### Windows

**Démarrer le Serveur :**
```cmd
start-server.bat
```

**Démarrer un Client :**
```cmd
start-client.bat
```

### Linux / Mac

**Rendre les scripts exécutables (une seule fois) :**
```bash
chmod +x *.sh
```

**Démarrer le Serveur :**
```bash
./start-server.sh
```

**Démarrer un Client :**
```bash
./start-client.sh
```

---

## ❓ Problèmes Fréquents

### "Cannot find or load main class"
**Solution :**
```bash
mvn clean compile
```

### "Port 1099 already in use"
**Solution :** Un serveur tourne déjà
```bash
# Windows
netstat -ano | findstr :1099
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :1099
kill -9 <PID>
```

### "JavaFX runtime components are missing"
**Solution :**
```bash
mvn clean install
```

### Le client ne se connecte pas
**Solution :** Vérifiez que le serveur est démarré AVANT le client

---

## 📖 Pour Aller Plus Loin

Une fois que tout fonctionne, consultez :

- **README.md** - Documentation complète
- **INTELLIJ_SETUP.md** - Configuration avancée d'IntelliJ
- **ARCHITECTURE.md** - Comprendre comment ça marche
- **IMPROVEMENTS.md** - Ajouter des fonctionnalités

---

## 🎓 Exercices Pratiques

### Niveau 1 - Débutant
- [ ] Lancer 3 clients et faire une conversation
- [ ] Changer les couleurs de l'interface
- [ ] Modifier le titre de la fenêtre

### Niveau 2 - Intermédiaire
- [ ] Ajouter un compteur d'utilisateurs connectés
- [ ] Changer le format de l'horodatage
- [ ] Ajouter un bouton "Effacer" pour nettoyer l'historique

### Niveau 3 - Avancé
- [ ] Implémenter la liste des utilisateurs (voir IMPROVEMENTS.md)
- [ ] Ajouter des messages privés
- [ ] Créer un système d'émojis

---

## 🏆 Checklist de Succès

- [ ] Le serveur démarre sans erreur
- [ ] Un client peut se connecter
- [ ] Plusieurs clients peuvent discuter ensemble
- [ ] Les messages sont horodatés
- [ ] Les notifications d'arrivée/départ fonctionnent
- [ ] La déconnexion est propre

**Si toutes les cases sont cochées : Félicitations ! Votre chat RMI fonctionne parfaitement ! 🎉**

---

**Temps total estimé : 3-5 minutes**  
**Difficulté : Facile** ⭐  
**Prérequis : Java 17, Maven**
