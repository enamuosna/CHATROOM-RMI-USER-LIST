package com.chat.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface distante représentant un utilisateur du chat.
 * Permet à la salle de discussion d'envoyer des messages aux clients.
 */
public interface ChatUser extends Remote {
    /**
     * Affiche un message reçu de la salle de discussion.
     * 
     * @param message Le message à afficher
     * @throws RemoteException En cas d'erreur de communication RMI
     */
    void displayMessage(String message) throws RemoteException;
    
    /**
     * Met à jour la liste des utilisateurs connectés.
     * 
     * @param users Liste des pseudonymes des utilisateurs connectés
     * @throws RemoteException En cas d'erreur de communication RMI
     */
    void updateUserList(List<String> users) throws RemoteException;
}
