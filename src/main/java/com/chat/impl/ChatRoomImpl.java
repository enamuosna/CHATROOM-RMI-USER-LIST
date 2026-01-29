package com.chat.impl;

import com.chat.interfaces.ChatRoom;
import com.chat.interfaces.ChatUser;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implémentation de la salle de discussion.
 * Gère les abonnements des utilisateurs et la diffusion des messages.
 */
public class ChatRoomImpl extends UnicastRemoteObject implements ChatRoom {
    
    private static final long serialVersionUID = 1L;
    private final Map<String, ChatUser> users;
    private final DateTimeFormatter timeFormatter;
    
    public ChatRoomImpl() throws RemoteException {
        super();
        this.users = new HashMap<>();
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }
    
    @Override
    public synchronized void subscribe(ChatUser user, String pseudo) throws RemoteException {
        if (users.containsKey(pseudo)) {
            throw new RemoteException("Le pseudo '" + pseudo + "' est déjà utilisé.");
        }
        
        users.put(pseudo, user);
        System.out.println("[" + getCurrentTime() + "] " + pseudo + " a rejoint la salle de discussion.");
        
        // Notifier tous les utilisateurs de l'arrivée du nouveau membre
        String joinMessage = "*** " + pseudo + " a rejoint la discussion ***";
        broadcastMessage(joinMessage);
        
        // Notifier tous les utilisateurs de la mise à jour de la liste
        notifyUserListUpdate();
    }
    
    @Override
    public synchronized void unsubscribe(String pseudo) throws RemoteException {
        if (users.remove(pseudo) != null) {
            System.out.println("[" + getCurrentTime() + "] " + pseudo + " a quitté la salle de discussion.");
            
            // Notifier tous les utilisateurs du départ
            String leaveMessage = "*** " + pseudo + " a quitté la discussion ***";
            broadcastMessage(leaveMessage);
            
            // Notifier tous les utilisateurs de la mise à jour de la liste
            notifyUserListUpdate();
        }
    }
    
    @Override
    public synchronized void postMessage(String pseudo, String message) throws RemoteException {
        if (!users.containsKey(pseudo)) {
            throw new RemoteException("L'utilisateur '" + pseudo + "' n'est pas abonné.");
        }
        
        String formattedMessage = "[" + getCurrentTime() + "] " + pseudo + ": " + message;
        System.out.println(formattedMessage);
        
        // Diffuser le message à tous les utilisateurs
        broadcastMessage(formattedMessage);
    }
    
    @Override
    public synchronized List<String> getConnectedUsers() throws RemoteException {
        return new ArrayList<>(users.keySet());
    }
    
    /**
     * Diffuse un message à tous les utilisateurs connectés.
     */
    private void broadcastMessage(String message) {
        // Créer une copie de la liste des utilisateurs pour éviter les modifications concurrentes
        Map<String, ChatUser> usersCopy = new HashMap<>(users);
        
        for (Map.Entry<String, ChatUser> entry : usersCopy.entrySet()) {
            try {
                entry.getValue().displayMessage(message);
            } catch (RemoteException e) {
                System.err.println("Erreur lors de l'envoi du message à " + entry.getKey() + ": " + e.getMessage());
                // Supprimer l'utilisateur déconnecté
                users.remove(entry.getKey());
            }
        }
    }
    
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
                System.err.println("Erreur lors de la mise à jour de la liste pour " + entry.getKey() + ": " + e.getMessage());
                users.remove(entry.getKey());
            }
        }
    }
    
    /**
     * Retourne l'heure actuelle formatée.
     */
    private String getCurrentTime() {
        return LocalDateTime.now().format(timeFormatter);
    }
    
    /**
     * Retourne le nombre d'utilisateurs connectés.
     */
    public synchronized int getUserCount() {
        return users.size();
    }
}
