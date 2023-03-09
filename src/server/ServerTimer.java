package server;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import shared.interfaces.server.GameServer;

public class ServerTimer implements Runnable {

    private Thread timer;

    private GameServer server;

    private boolean ready;

    public ServerTimer(Registry registry) throws AccessException, RemoteException, NotBoundException {

        this.server = (GameServer)registry.lookup("GameServer");
        System.out.println("Server booted... Waiting for players to connect!");
        while(!ready){ready = server.ready();}
        System.out.println("\n\n\n\n\n\n\n");
        System.out.print("\33[1A\33[2K");

    }

    @Override
    public void run() {

        while(timer!=null){

            try
            {

                Thread.sleep(1_000);
                printDebug();

            } catch (Exception e) {

                e.printStackTrace();

            }


        }
        
    }

    public void startTimer(){

        timer = new Thread(this);
        timer.start();
        System.out.println("timerstarted");
        System.out.print("\n\n\n\n\n");
    
    }

    public void printDebug() throws RemoteException{

        server.ping();
        server.printDebug();

    }

    
}