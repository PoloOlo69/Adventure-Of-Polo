package server;

import shared.interfaces.client.RemotePlayer;
import shared.interfaces.client.RemoteProjectileManager;
import shared.interfaces.server.ExchangeServer;
import shared.records.PlayerRecord;
import shared.records.ProjectileRecord;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class ExchangeServerImpl implements ExchangeServer {

    public ConcurrentHashMap<String, RemotePlayer> players = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, RemoteProjectileManager> pms = new ConcurrentHashMap<>();

    public ExchangeServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this,0);
    }

    @Override
    public <T> void register(T remoteObject, String objectID) throws RemoteException {

        GameServerImpl.requests++;

        if(remoteObject instanceof RemotePlayer p)
        {
            players.put(objectID, p);
        }
        else if(remoteObject instanceof RemoteProjectileManager pm)
        {
            pms.put(objectID, pm);
        }
        else
        {
            throw new NoSuchObjectException(remoteObject.getClass().toString());
        }
    }
    @Override
    public <T> void forwardState(String source, T record) throws RemoteException {

        GameServerImpl.requests++;

        if(record instanceof PlayerRecord state)
        {
            players.get(resolve(source)).receiveState(state);
        }
        else if(record instanceof ProjectileRecord projectile)
        {
            pms.get(resolve(source)).receiveProjectile(projectile);
        }
        else
        {
            throw new NoSuchElementException(record.getClass().toString());
        }

    }

    @Override
    public void getPlayerState(String from) throws RemoteException {
        GameServerImpl.requests++;
        players.get(resolve(from)).sendState();
    }

    @Override
    public void removeProjectile(String source) throws RemoteException {
        GameServerImpl.requests++;
        pms.get(resolve(source)).remove(resolve(source));
        pms.forEach((k, v)-> {
            try {
                v.remove(source);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String resolve(String name){

        GameServerImpl.namesResolved++;

        switch(name){

            case "c1player1": return "c2player2";
            case "c1player2": return "c2player1";
            case "c2player1": return "c1player2";
            case "c2player2": return "c1player1";

            case "client1pm": return "client2pm";
            case "client2pm": return "client1pm";

        }

        throw new NoSuchElementException(name + "does not exist");
    }

}
