package src.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.Arrays;

import javax.swing.JFrame;

import shared.interfaces.server.ExchangeServer;
import shared.interfaces.server.GameServer;


public class Main {
/*^
         ▄███████▄  ▄██████▄   ▄█        ▄██████▄        ▄█    █▄     ▄████████      ▀█████████▄   ▄██████▄   ▄█        ▄██████▄  
        ███    ███ ███    ███ ███       ███    ███      ███    ███   ███    ███        ███    ███ ███    ███ ███       ███    ███ 
        ███    ███ ███    ███ ███       ███    ███      ███    ███   ███    █▀         ███    ███ ███    ███ ███       ███    ███ 
        ███    ███ ███    ███ ███       ███    ███      ███    ███   ███              ▄███▄▄▄██▀  ███    ███ ███       ███    ███
      ▀█████████▀  ███    ███ ███       ███    ███      ███    ███ ▀███████████      ▀▀███▀▀▀██▄  ███    ███ ███       ███    ███ 
        ███        ███    ███ ███       ███    ███      ███    ███          ███        ███    ██▄ ███    ███ ███       ███    ███ 
        ███        ███    ███ ███▌    ▄ ███    ███      ███    ███    ▄█    ███ ████   ███    ███ ███    ███ ███▌    ▄ ███    ███ 
       ▄████▀       ▀██████▀  █████▄▄██  ▀██████▀        ▀██████▀   ▄████████▀  ████  ▄█████████▀  ▀██████▀  █████▄▄██  ▀██████▀  
*/     
        public static void main(String... args) throws RemoteException, NotBoundException {
        
                new SoundManager();
                new CollisionChecker();

                String host = (args.length <= 1) ? "localhost" : Arrays.toString(args);
                JFrame window = new JFrame();
                
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                window.setResizable(false);
                window.setTitle("Polo vs. Bolo Multiplayer Client");
                
                // 1099 is the Default port RMI Interface is listening to
                Registry registry = LocateRegistry.getRegistry(host, 1099); 
                GameServer server = (GameServer) registry.lookup("GameServer");
                ExchangeServer exchange = server.getExchange();

                GamePanel gp = new GamePanel(server, exchange);

                window.add(gp);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);

                gp.startGameThread();

                System.out.println("Setup successful!");
        
        }
}
