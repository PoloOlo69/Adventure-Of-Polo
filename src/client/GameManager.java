package client;

import shared.interfaces.client.RemoteGameManager;
import shared.interfaces.server.GameServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameManager implements RemoteGameManager {
    private GameServer server = null;
    private GamePanel gamePanel = null;
    private String managerID;

    public GameManager(GamePanel gamePanel, GameServer server, String managerID) throws RemoteException {
        UnicastRemoteObject.exportObject(this,0);
        this.gamePanel = gamePanel;
        this.server = server;
        this.managerID = managerID;
    }


    @Override
    public long ping() throws RemoteException {
        return System.nanoTime();
    }
}
