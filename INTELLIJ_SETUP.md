# Guide de Configuration IntelliJ IDEA

## Configuration Initiale du Projet

### 1. Importer le Projet

1. Ouvrez IntelliJ IDEA
2. Cliquez sur `File > Open...`
3. Naviguez vers le dossier `chatroom-rmi`
4. Sélectionnez le dossier et cliquez sur `OK`
5. IntelliJ détectera automatiquement le fichier `pom.xml`
6. Dans la boîte de dialogue, sélectionnez `Open as Project`

### 2. Configuration du JDK

1. Allez dans `File > Project Structure...` (Ctrl+Alt+Shift+S)
2. Dans la section `Project` :
   - **Project SDK** : Sélectionnez Java 17 (ou téléchargez-le si nécessaire)
   - **Project language level** : 17 - Sealed types, always-strict floating-point semantics
3. Cliquez sur `Apply` puis `OK`

### 3. Configuration de Maven

1. IntelliJ devrait automatiquement détecter le projet Maven
2. Si ce n'est pas le cas, clic droit sur `pom.xml` > `Add as Maven Project`
3. Attendez que Maven télécharge toutes les dépendances
4. Vous pouvez voir la progression dans l'onglet `Maven` (côté droit)

### 4. Vérifier l'Installation de JavaFX

Maven devrait télécharger automatiquement JavaFX. Pour vérifier :
1. Ouvrez l'onglet `Maven` (côté droit)
2. Déroulez `chatroom-rmi > Dependencies`
3. Vérifiez la présence de `org.openjfx:javafx-controls` et `javafx-fxml`

## Créer les Configurations de Lancement

### Configuration pour le Serveur

1. Cliquez sur `Run > Edit Configurations...`
2. Cliquez sur le `+` en haut à gauche
3. Sélectionnez `Application`
4. Configurez comme suit :
   - **Name** : `ChatServer`
   - **Main class** : `com.chat.server.ChatServer` (utilisez le bouton `...` pour chercher)
   - **Module** : `chatroom-rmi`
   - **JRE** : Use project JDK (17)
   - **Working directory** : Le dossier du projet
5. Cliquez sur `Apply` puis `OK`

### Configuration pour le Client

1. Cliquez sur `Run > Edit Configurations...`
2. Cliquez sur le `+` en haut à gauche
3. Sélectionnez `Application`
4. Configurez comme suit :
   - **Name** : `ChatClient`
   - **Main class** : `com.chat.client.ChatUserImpl$ChatApplication`
   - **Module** : `chatroom-rmi`
   - **JRE** : Use project JDK (17)
   - **VM options** : (laisser vide, Maven gère JavaFX)
   - **Working directory** : Le dossier du projet
5. Cliquez sur `Apply` puis `OK`

### Configuration Maven Alternative pour le Client

Si vous préférez utiliser le plugin Maven JavaFX :

1. Cliquez sur `Run > Edit Configurations...`
2. Cliquez sur le `+` en haut à gauche
3. Sélectionnez `Maven`
4. Configurez comme suit :
   - **Name** : `ChatClient (Maven)`
   - **Working directory** : Le dossier du projet
   - **Command line** : `javafx:run`
5. Cliquez sur `Apply` puis `OK`

## Lancer l'Application

### Méthode 1 : Via les Configurations

1. **Démarrer le serveur** :
   - Sélectionnez `ChatServer` dans la liste déroulante en haut
   - Cliquez sur le bouton vert `Run` (ou Shift+F10)
   - Vous devriez voir dans la console : "Serveur de chat démarré avec succès !"

2. **Démarrer le(s) client(s)** :
   - Sélectionnez `ChatClient` dans la liste déroulante
   - Cliquez sur le bouton vert `Run`
   - Pour démarrer plusieurs clients, cliquez sur la flèche à côté de `Run` et sélectionnez `Run 'ChatClient'` à nouveau

### Méthode 2 : Via le Navigateur de Projet

1. **Serveur** :
   - Naviguez vers `src/main/java/com/chat/server/ChatServer.java`
   - Clic droit sur le fichier ou sur la classe
   - Sélectionnez `Run 'ChatServer.main()'`

2. **Client** :
   - Naviguez vers `src/main/java/com/chat/client/ChatUserImpl.java`
   - Clic droit sur le fichier ou sur la classe interne `ChatApplication`
   - Sélectionnez `Run 'ChatUserImpl$ChatApplication.main()'`

### Méthode 3 : Via Maven

1. Ouvrez l'onglet `Maven` (côté droit)
2. Déroulez `chatroom-rmi > Plugins`
3. **Pour le serveur** :
   - Déroulez `exec`
   - Double-cliquez sur `exec:java`
   - Dans la console, le serveur démarre
4. **Pour le client** :
   - Déroulez `javafx`
   - Double-cliquez sur `javafx:run`

## Déboguer l'Application

### Déboguer le Serveur

1. Ouvrez `ChatServer.java`
2. Placez un point d'arrêt (clic sur la marge gauche) à la ligne souhaitée
3. Clic droit sur `ChatServer` > `Debug 'ChatServer.main()'`
   Ou utilisez la configuration et cliquez sur le bouton `Debug`

### Déboguer le Client

1. Ouvrez `ChatUserImpl.java`
2. Placez des points d'arrêt
3. Clic droit > `Debug` ou utilisez la configuration de débogage

## Problèmes Courants et Solutions

### Problème : "Cannot resolve symbol 'javafx'"

**Solution** :
1. Allez dans `File > Invalidate Caches...`
2. Sélectionnez `Invalidate and Restart`
3. Attendez le redémarrage et la réindexation
4. Si le problème persiste, vérifiez dans l'onglet Maven que les dépendances sont bien téléchargées

### Problème : "Error: JavaFX runtime components are missing"

**Solution** :
1. Vérifiez que le `pom.xml` contient bien les dépendances JavaFX
2. Synchronisez Maven : clic droit sur `pom.xml` > `Maven > Reload Project`
3. Si vous utilisez une configuration Application au lieu de Maven, ajoutez les VM options :
   ```
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
   ```

### Problème : "java.rmi.ConnectException"

**Solution** :
1. Assurez-vous que le serveur est démarré AVANT le client
2. Vérifiez que le port 1099 n'est pas utilisé par une autre application
3. Vérifiez votre pare-feu

### Problème : Maven ne télécharge pas les dépendances

**Solution** :
1. Vérifiez votre connexion Internet
2. Allez dans `File > Settings > Build, Execution, Deployment > Build Tools > Maven`
3. Vérifiez que `Use plugin registry` n'est pas coché
4. Cliquez sur `Reimport All Maven Projects` dans l'onglet Maven

## Raccourcis Clavier Utiles

- **Compiler** : Ctrl+F9
- **Run** : Shift+F10
- **Debug** : Shift+F9
- **Stop** : Ctrl+F2
- **Trouver une classe** : Ctrl+N
- **Aller à la déclaration** : Ctrl+B
- **Refactoriser/Renommer** : Shift+F6
- **Formater le code** : Ctrl+Alt+L

## Fonctionnalités IntelliJ Utiles

### Auto-Import

IntelliJ peut automatiquement importer les classes :
1. `File > Settings > Editor > General > Auto Import`
2. Cochez `Add unambiguous imports on the fly`
3. Cochez `Optimize imports on the fly`

### Code Templates

IntelliJ offre des templates pour générer du code rapidement :
- `psvm` + Tab : génère `public static void main(String[] args)`
- `sout` + Tab : génère `System.out.println()`
- `fori` + Tab : génère une boucle for

### Inspection du Code

1. `Analyze > Inspect Code...`
2. Sélectionnez le scope (tout le projet ou un module)
3. IntelliJ analysera votre code et suggérera des améliorations

## Structure Recommandée

Voici comment organiser vos fenêtres IntelliJ :

1. **Gauche** : Navigateur de projet (Alt+1)
2. **Centre** : Éditeur de code
3. **Droite** : Onglet Maven (View > Tool Windows > Maven)
4. **Bas** : Console de sortie / Débogueur
5. **Bas-Droite** : Messages de build

## Conseils pour le Développement

1. **Utilisez le formatage automatique** : Ctrl+Alt+L régulièrement
2. **Activez les annotations** : IntelliJ suggère automatiquement @Override
3. **Utilisez les Live Templates** : Accélérez l'écriture du code
4. **Profitez de l'auto-complétion** : Ctrl+Space
5. **Refactorisez intelligemment** : Utilisez les outils de refactoring d'IntelliJ

## Ressources Supplémentaires

- [Documentation JavaFX](https://openjfx.io/)
- [Documentation Java RMI](https://docs.oracle.com/javase/tutorial/rmi/)
- [IntelliJ IDEA Documentation](https://www.jetbrains.com/idea/documentation/)
- [Maven Documentation](https://maven.apache.org/guides/)
