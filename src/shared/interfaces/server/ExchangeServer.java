package shared.interfaces.server;

import shared.interfaces.client.RemotePlayer;
import shared.interfaces.client.RemoteProjectileManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ExchangeServer extends Remote {

    <T> void register(T remoteObject, String objectID) throws RemoteException;

    <T> void forwardState(String caller, T record) throws RemoteException;

    void getPlayerState(String from) throws RemoteException;

    void removeProjectile(String from) throws RemoteException;

}
