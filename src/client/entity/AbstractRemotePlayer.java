package client.entity;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client.SoundManager;
import shared.interfaces.client.RemotePlayer;

public abstract class AbstractRemotePlayer implements RemotePlayer{

    AbstractRemotePlayer() throws RemoteException{
       UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void playSound(String sound) throws RemoteException {
        SoundManager.playSound(sound);
    }
    
    @Override
    public abstract void sendState() throws RemoteException;

}
