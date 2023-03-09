package shared.interfaces.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.GameManager;
import shared.interfaces.client.RemoteGameManager;
import shared.interfaces.client.RemotePlayer;
import shared.interfaces.client.RemoteProjectileManager;
import shared.records.PlayerRecord;
import shared.records.ProjectileRecord;

public interface GameServer extends Remote {
    
    String assignPlayer() throws RemoteException;
    
    boolean ready() throws RemoteException;

    ExchangeServer getExchange() throws RemoteException;

    void printDebug() throws RemoteException;

    void register(RemoteGameManager gm, String managerID) throws RemoteException;
    void ping() throws RemoteException;

}
