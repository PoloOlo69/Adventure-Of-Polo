package shared.interfaces.client;

import shared.records.PlayerRecord;
import java.rmi.RemoteException;

import java.rmi.Remote;


public interface RemotePlayer extends Remote {

   void sendState() throws RemoteException;

   void receiveState(PlayerRecord p) throws RemoteException;

   void playSound(String sound) throws RemoteException;

   void reduceLives() throws RemoteException;
}
