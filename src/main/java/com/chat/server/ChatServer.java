package com.chat.server;

import com.chat.impl.ChatRoomImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Serveur de la salle de discussion RMI.
 * Crée et enregistre la salle de discussion dans le registre RMI.
 */
public class ChatServer {
    
    private static final int PORT = 1099;
    private static final String BINDING_NAME = "ChatRoom";
    
    public static void main(String[] args) {
        try {
            System.out.println("Démarrage du serveur de chat...");
            
            // Créer l'instance de la salle de discussion
            ChatRoomImpl chatRoom = new ChatRoomImpl();
            
            // Créer le registre RMI sur le port spécifié
            Registry registry = LocateRegistry.createRegistry(PORT);
            
            // Enregistrer la salle de discussion dans le registre
            registry.rebind(BINDING_NAME, chatRoom);
            
            System.out.println("Serveur de chat démarré avec succès !");
            System.out.println("Port: " + PORT);
            System.out.println("Nom de liaison: " + BINDING_NAME);
            System.out.println("En attente de connexions...");
            System.out.println("Appuyez sur Ctrl+C pour arrêter le serveur.");
            
            // Garder le serveur actif
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage du serveur: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
