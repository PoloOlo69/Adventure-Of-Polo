package client;

import java.rmi.RemoteException;

import shared.interfaces.server.ExchangeServer;
import shared.interfaces.server.GameServer;
import shared.records.Point;
import shared.records.Rectangle;

import client.entity.Player;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;


public class GamePanel extends JPanel implements Runnable {
    
    // Debug
    private long nextSecond;
    private int frames;

    // SETTINGS
    public static final Rectangle SOLID_AREA    = new Rectangle(20, 20, 20, 20);
    public static final Point P1_DEFAULT_POS    = new Point(256, 128);
    public static final Point P2_DEFAULT_POS    = new Point(512, 128);
    public static final Point COLLECTED         = new Point(0,0);
    public static final String P1 = "player1";
    public static final String P2 = "player2";
    public static final String C1PM = "client1pm";
    public static final String C2PM = "client2pm";
    public static final String C1GM = "client1gm";
    public static final String C2GM = "client2gm";
    public static final String C1P1 = "c1player1";
    public static final String C1P2 = "c1player2";
    public static final String C2P1 = "c2player1";
    public static final String C2P2 = "c2player2";
    public static final int TILE_SIZE = 64; // 64x64 tiles
    public static final int MAX_HORIZONTAL_TILES = 16;
    public static final int MAX_VERTICAL_TILES = 12;
    public static final int MAX_WIDTH = MAX_HORIZONTAL_TILES * TILE_SIZE; // 1024p
    public static final int FPS = 60;
    public static final int MAX_HEIGHT = MAX_VERTICAL_TILES * TILE_SIZE; // 768p

    // DEPENDENCIES
    private Thread gameThread;
    public Player player = null;
    public Player opponent = null;
    public ProjectileManager projectileManager = null;
    public GameManager gameManager = null;
    public TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler();
    
    public GamePanel(GameServer server, ExchangeServer exchange) throws RemoteException{
        super();
        this.clientSetup(server, exchange, keyHandler);
        this.addKeyListener(keyHandler);
        this.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }
    
    public void clientSetup(GameServer server, ExchangeServer exchange, KeyHandler kh) throws RemoteException{

        switch (server.assignPlayer()) {

            case P1 ->
            {
                player = new Player.Polo(this, kh, exchange, C1P1, P1, P1_DEFAULT_POS, COLLECTED, SOLID_AREA);
                exchange.register(player, C1P1);
                opponent = new Player.Bolo(this, null, exchange, C1P2, P2, P2_DEFAULT_POS, COLLECTED, SOLID_AREA);
                exchange.register(opponent, C1P2);
                gameManager = new GameManager(this, server, C1GM);
                server.register(gameManager, C1GM);
                projectileManager = new ProjectileManager(this, exchange, kh, C1PM);
                exchange.register(projectileManager, C1PM);
            }

            case P2 ->
            {
                player = new Player.Bolo(this, kh, exchange, C2P1, P2, P2_DEFAULT_POS, COLLECTED, SOLID_AREA);
                exchange.register(player, C2P1);
                opponent = new Player.Polo(this, null, exchange, C2P2, P1, P1_DEFAULT_POS, COLLECTED, SOLID_AREA);
                exchange.register(opponent, C2P2);
                gameManager = new GameManager(this, server, C2GM);
                server.register(gameManager, C2GM);
                projectileManager = new ProjectileManager(this, exchange, kh, C2PM);
                exchange.register(projectileManager, C2PM);
            }

        }
            System.out.println("\nWaiting for player 2 to join...");
            while(true){if(server.ready()) break;}
            for(int i = 0; i < 5; i++){System.out.print("\33[1A\33[2K");}
            SoundManager.playTheme();
    }

    public void startGameThread() {
        
        gameThread = new Thread(this);
        gameThread.start();

    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS; // 1 BILLION NANOSECONDS / 60 FPS
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null)
        {       
        
            try
            {
                // UPDATE
                update();
                
                // DRAW
                repaint();

                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1_000_000; // CONVERSION INTO MILLISECONDS

                if (remainingTime < 0)
                {
                    remainingTime = 0;
                } 

                Thread.sleep((long) remainingTime);

                nextDrawTime += drawInterval;

            } catch (Exception e) {

                e.printStackTrace();

            }

        }

    }
    
    public void update() throws RemoteException{
        
        debugInfo();
        
        player.update();
        
        opponent.getState();

        projectileManager.update(player.pos);


    }

    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        
    	Toolkit.getDefaultToolkit().sync();
        
        tileManager.draw(g2);
        
        player.draw(g2);

        opponent.draw(g2);
        
        projectileManager.draw(g2);

        g2.dispose();
    }

    public void debugInfo() throws RemoteException{

        if(System.nanoTime() > nextSecond)
        {
            this.nextSecond = System.nanoTime() + 1_000_000_000;
            System.out.print("\33[1A\33[2K");
            System.out.println(frames +"FPS");
            frames = 0;
        }
        frames++;
    }
}
