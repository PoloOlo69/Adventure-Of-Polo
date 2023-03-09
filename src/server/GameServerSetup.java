package server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import shared.interfaces.server.GameServer;

// A main Method for creating an instance of the GameServerImpl and exporting this instance to the registry
public class GameServerSetup {
    
    public static void main(String... args) throws AlreadyBoundException, NotBoundException{
        try
        {
            // Create Server Object that the client can invoke methods from
            GameServer server = new GameServerImpl();
            
            // 1099 is the default RMI port this should work
            Registry registry = LocateRegistry.createRegistry(1099);

            // Binds a String to the instance of the object and exports it to the local registy
            // Any client is now able to find a reference for this object in the registry
            registry.bind("GameServer", server);

            ServerTimer timer = new ServerTimer(registry);

            timer.startTimer();

        } catch (RemoteException e) {
            System.out.println("Something went wrong while booting the Server...");
            e.printStackTrace();
        }
    }
}   
