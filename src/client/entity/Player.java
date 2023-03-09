package client.entity;

import shared.interfaces.server.ExchangeServer;
import shared.records.*;
import client.*;

import java.awt.Graphics2D;
import java.io.IOException;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class Player extends AbstractRemotePlayer{
    
    private BufferedImage l1, l2, l3, l4, l5, r1, r2, r3, r4, r5, u1, u2;
    private BufferedImage full, half, blank;
    private final BufferedImage[] hearts = new BufferedImage[2];
    private final GamePanel gp;
    private final ExchangeServer exchange;
    private final KeyHandler kh;
    public Rectangle hitbox;
    public Point collected;
    public Point pos;
    public String playerID;
    public String direction;
    public String imageName;
    public boolean moving;
    public boolean collision;
    public boolean attackPerformed;
    public int lives;
    public int velocity;
    public int spriteCounter;
    public int attackCounter;
    public PlayerRecord playerState;

    public Player(GamePanel gp, KeyHandler kh, ExchangeServer exchange, String playerID, String imageName, Point pos, Point collected, Rectangle hitbox) throws RemoteException{
        super();
        this.imageName = imageName;
        this.gp = gp;
        this.kh = kh;
        this.exchange = exchange;
        this.playerID = playerID;
        this.pos = pos;
        this.collected = collected;
        this.hitbox = hitbox;
        this.moving = false;
        this.direction = "up";
        this.collision = false;
        this.lives = 4;
        this.velocity = 4;
        this.spriteCounter = 0;
        getImages();

        hearts[0] = full;
        hearts[1] = full;

    }

    public void getImages(){

        System.out.print("\nLoading " + playerID + " images... ");
        try{

            l1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_l1.png")));
            l2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_l2.png")));
            l4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_l4.png")));
            l5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_l5.png")));
            l3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_l3.png")));
            r1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_r1.png")));
            r2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_r2.png")));
            r3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_r3.png")));
            r4 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_r4.png")));
            r5 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_r5.png")));
            u1 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_u1.png")));
            u2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+imageName+"_u2.png")));

            full = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/full_heart.png")));
            half  = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/half_heart.png")));
            blank = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/blank_heart.png")));

            System.out.print("finished!");
        } catch (IOException e) {
            System.out.print("\nFailed loading " + playerID + " images ");
            e.printStackTrace();
        }

    }

    // THIS IS NO GOOD PRACTICE YOU SHOULD IMPLEMENT METHODS FOR REMOVING TILES!
    @Override
    public void sendState() throws RemoteException {

        playerState = new PlayerRecord(playerID, direction, moving, pos, collected, spriteCounter, lives);

        exchange.forwardState(playerID, playerState);

        if(!collected.equals(GamePanel.COLLECTED))
        {
            collected = GamePanel.COLLECTED;
        }

    }

    // ALSO NO GOOD PRACTICE I SHOULD IMPLEMENT METHODS TO MAKE THESE IF STATEMENTS UNNECESSARY!
    @Override
    public void receiveState(PlayerRecord state) throws RemoteException{

        playerState = state;

        pos = state.pos();
        moving = state.moving();
        direction = state.direction();
        collected = state.collected();
        spriteCounter = state.spriteCounter();

        if (!collected.equals(GamePanel.COLLECTED))
        {
            TileManager.removeTile(collected);
            SoundManager.playSound("yeah");
            collected = GamePanel.COLLECTED;
        }

        if (lives != state.lives())
        {
            lives = state.lives();
            updateLives();
        }

    }

    @Override
    public void reduceLives() throws RemoteException {
        lives--;
        updateLives();
    }


    public void updateLives(){
        switch(lives){
            case 4 -> hearts[1] = full;
            case 3 -> hearts[1] = half;
            case 2 -> hearts[1] = blank;
            case 1 -> hearts[0] = half;
            case 0 -> hearts[0] = blank;
        }
    }

    public void getState() throws RemoteException{
        exchange.getPlayerState(playerID);
    }
    
    public void update() throws RemoteException{
        
        moveEntity();

        count();

    }

    public boolean isMoving(){
        return kh.downPressed||kh.leftPressed||kh.upPressed||kh.rightPressed;
    }
    
    public void moveEntity() throws RemoteException{
        
        moving = isMoving();

        // IF ELSE STATEMENTS WOULD INHIBIT DIAGONAL WALKING
        if(kh.upPressed)
        {
            this.direction = "up";
        }
        if(kh.downPressed)
        {
            this.direction = "down";  
        }
        if(kh.leftPressed)
        {
            this.direction = "left";
        }
        if(kh.rightPressed)
        {
            this.direction = "right";
        }
        if(kh.spacePressed)
        {
            attack();
        }

        
        collision = false;
        CollisionChecker.checkTile(this);

        if(!collision)
        {
            int x = pos.x(), y = pos.y();
            
            if (kh.upPressed)
            {
                y -= velocity;
            }
            if (kh.downPressed)
            {
                y += velocity;
            }
            if (kh.leftPressed)
            {
                x -= velocity;
            }
            if (kh.rightPressed)
            {
                x += velocity;
            }


            this.pos = new Point(x, y);

        }
    }

    public void drawLife(Graphics2D g2) {

        g2.drawImage(hearts[0], pos.x()+16, pos.y()-16, 16, 16, null);
        g2.drawImage(hearts[1], pos.x()+32, pos.y()-16, 16, 16, null);

    }

    public void draw(Graphics2D g2){

        BufferedImage image = null;
        
        switch (direction){

            case "right":

                if(!moving)
                {
                    image = r1;
                    spriteCounter = 0;
                    break;
                }

                if(spriteCounter >= 80)spriteCounter = 0;
            
                if(spriteCounter >= 0  && spriteCounter < 10)
                {
                    image = r1;
                    break;
                }
                if(spriteCounter >= 10 && spriteCounter < 20)
                {
                    image = r2;
                    break;
                }
                if(spriteCounter >= 20 && spriteCounter < 30)
                {
                    image = r3;
                    break;
                }
                if(spriteCounter >= 30 && spriteCounter < 40)
                {
                    image = r2;
                    break;
                }
                if(spriteCounter >= 40 && spriteCounter < 50)
                {
                    image = r2;
                    break;
                }
                if(spriteCounter >= 50 && spriteCounter < 60)
                {
                    image = r4;
                    break;
                }
                if(spriteCounter >= 60 && spriteCounter < 70)
                {
                    image = r5;
                    break;
                }
                if(spriteCounter >= 70)
                {
                    image = r4;
                    break;
                }
            
            case "left":

                if(!moving)
                {
                    image = l1;
                    spriteCounter = 0;
                    break;
                }

                if(spriteCounter >= 80)spriteCounter = 0;

                if(spriteCounter >= 0  && spriteCounter < 10)
                {
                    image = l1;
                    break;
                }
                if(spriteCounter >= 10 && spriteCounter < 20)
                {
                    image = l2;
                    break;
                }
                if(spriteCounter >= 20 && spriteCounter < 30)
                {
                    image = l3;
                    break;
                }
                if(spriteCounter >= 30 && spriteCounter < 40)
                {
                    image = l2;
                    break;
                }
                if(spriteCounter >= 40 && spriteCounter < 50)
                {
                    image = l2;
                    break;
                }
                if(spriteCounter >= 50 && spriteCounter < 60)
                {
                    image = l4;
                    break;
                }
                if(spriteCounter >= 60 && spriteCounter < 70)
                {
                    image = l5;
                    break;
                }
                if(spriteCounter >= 70)
                {
                    image = l4;
                    break;
                }

            default:
                
                if(spriteCounter >= 80) spriteCounter = 0;

                if(!moving)
                {
                    image = u1;
                    spriteCounter = 0;
                    break;
                }

                if(spriteCounter >= 0  && spriteCounter < 40)
                {
                    image = u1;
                    break;
                }
                if(spriteCounter >= 40)
                {
                    image = u2;
                    break;
                }
            }

        g2.drawImage(image, pos.x(), pos.y(), gp);

        drawLife(g2);

    }

    public void attack() throws RemoteException{
        if(!attackPerformed&&kh.spacePressed){
            gp.projectileManager.newProjectile(pos, direction);
            attackPerformed = true;
        }
    }

    public void count() throws RemoteException{
        spriteCounter++;
        if(attackPerformed)
        {
            attackCounter++;
            if(attackCounter>60)
            {
                attackPerformed = false;
                attackCounter = 0;
            }
        }
    }

    public static final class Polo extends Player {

        public Polo(GamePanel gp, KeyHandler keyH, ExchangeServer server, String name, String imageName, Point pos, Point collected, Rectangle hitbox) throws RemoteException{
           super(gp, keyH, server, name, imageName, pos, collected, hitbox);
        }

    }

    public static final class Bolo extends Player {

        public Bolo(GamePanel gp,KeyHandler keyH, ExchangeServer server, String name,String imageName, Point pos, Point collected, Rectangle hitbox) throws RemoteException{
            super(gp, keyH, server, name, imageName, pos, collected, hitbox);
        }
    }
}
