package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import shared.interfaces.client.RemoteGameManager;
import shared.interfaces.client.RemotePlayer;
import shared.interfaces.client.RemoteProjectileManager;
import shared.interfaces.server.ExchangeServer;
import shared.interfaces.server.GameServer;
import shared.records.PlayerRecord;
import shared.records.ProjectileRecord;

// A Object implementing the GameServerInterface
public class GameServerImpl implements GameServer {

    private final ExchangeServerImpl exchange = new ExchangeServerImpl();
    public static long requests;
    public static long namesResolved;
    public static long c1ping;
    public static long c2ping;
    private static final String P1 = "player1";
    private static final String P2 = "player2";
    private volatile boolean ready = false;
    private volatile boolean p1connected = false;

    private final ConcurrentHashMap<String, RemoteGameManager> gms = new ConcurrentHashMap<>();
    public GameServerImpl() throws RemoteException{
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override 
    public boolean ready(){
        requests++;
        return ready;
    }

    @Override
    public String assignPlayer(){
        requests++;
        try {
            if(!p1connected){
                p1connected = true;
                return P1;
            } else {
                ready = true;
                return P2;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public ExchangeServer getExchange() throws RemoteException {
        requests++;
        return exchange;
    }

    @Override
    public void printDebug() throws RemoteException{

        requests++;

        System.out.print("\33[1A\33[2K");
        System.out.print("\33[1A\33[2K");
        System.out.print("\33[1A\33[2K");
        System.out.print("\33[1A\33[2K");
        System.out.print("\33[1A\33[2K");
        System.out.print("\33[1A\33[2K");
        System.out.print("\33[1A\33[2K");

        System.out.println(LocalDateTime.now());
        System.out.println("Client 1 Latency: " + c1ping + "nanos");
        System.out.println("Client 2 Latency: " + c2ping + "nanos");
        System.out.println("Requests/second: " + requests);
        System.out.println("Names Resolved/second: " + namesResolved);
        System.out.println("PlayerRecords exchanged: " + PlayerRecord.recordsSent());
        System.out.println("Projectiles exchanged: " + ProjectileRecord.recordsSent());

        requests = 0;
        namesResolved = 0;
    }

    @Override
    public void register(RemoteGameManager gm, String managerID) {

        requests++;

        gms.put(managerID,gm);

    }

    @Override
    public void ping() throws RemoteException {

        c1ping = latency(gms.get("client1gm").ping());
        c2ping = latency(gms.get("client2gm").ping());

    }
    private long latency(long latency) {
        return System.nanoTime() - latency;
    }

}


