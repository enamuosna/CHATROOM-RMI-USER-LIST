package com.chat.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Interface distante représentant une salle de discussion.
 * Permet aux utilisateurs de s'abonner, se désabonner et poster des messages.
 */
public interface ChatRoom extends Remote {
    /**
     * Permet à un utilisateur de s'abonner à la salle de discussion.
     * 
     * @param user La référence distante de l'utilisateur
     * @param pseudo Le pseudonyme de l'utilisateur
     * @throws RemoteException En cas d'erreur de communication RMI
     */
    void subscribe(ChatUser user, String pseudo) throws RemoteException;
    
    /**
     * Permet à un utilisateur de se désabonner de la salle de discussion.
     * 
     * @param pseudo Le pseudonyme de l'utilisateur à désabonner
     * @throws RemoteException En cas d'erreur de communication RMI
     */
    void unsubscribe(String pseudo) throws RemoteException;
    
    /**
     * Poste un message dans la salle de discussion.
     * Le message sera diffusé à tous les utilisateurs connectés.
     * 
     * @param pseudo Le pseudonyme de l'expéditeur
     * @param message Le message à diffuser
     * @throws RemoteException En cas d'erreur de communication RMI
     */
    void postMessage(String pseudo, String message) throws RemoteException;
    
    /**
     * Récupère la liste des utilisateurs actuellement connectés.
     * 
     * @return Liste des pseudonymes des utilisateurs connectés
     * @throws RemoteException En cas d'erreur de communication RMI
     */
    List<String> getConnectedUsers() throws RemoteException;
}
