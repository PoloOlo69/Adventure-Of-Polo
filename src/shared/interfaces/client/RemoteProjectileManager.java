package shared.interfaces.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

import shared.records.ProjectileRecord;

public interface RemoteProjectileManager extends Remote {
    
    void receiveProjectile(ProjectileRecord pr) throws RemoteException;

    void remove(String source) throws RemoteException;


}
