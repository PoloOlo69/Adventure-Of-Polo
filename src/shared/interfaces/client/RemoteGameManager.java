package shared.interfaces.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteGameManager extends Remote {

    public long ping() throws RemoteException;

}
