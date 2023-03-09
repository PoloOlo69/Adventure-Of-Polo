package client;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.awt.Graphics2D;
import java.util.concurrent.ConcurrentHashMap;

import shared.interfaces.client.RemoteProjectileManager;
import shared.interfaces.server.ExchangeServer;
import shared.records.Point;
import shared.records.ProjectileRecord;

import javax.imageio.ImageIO;

public class ProjectileManager implements RemoteProjectileManager {
    GamePanel gp;
    ExchangeServer exchange;
    KeyHandler kh;

    static String[] names = {"hearts", "spades", "clubs", "diamonds"};
    static Random rand = new Random();

    public String managerID;
    private final ConcurrentHashMap<String, Projectile> projectiles = new ConcurrentHashMap<>();
    
    public ProjectileManager(GamePanel gp, ExchangeServer exchange, KeyHandler kh, String managerID) throws RemoteException{
        UnicastRemoteObject.exportObject(this, 0);
        this.gp = gp;
        this.exchange = exchange;
        this.kh = kh;
        this.managerID = managerID;
    }

    public static String randName(){

        return names[rand.nextInt(4)];

    }

    public void draw(Graphics2D g2){

        projectiles.forEach((x,y)->y.draw(g2));

    }

    public void update(Point pos){
        projectiles.forEach((x,y)->
        {
            y.update();
            if(y.ttl<=0||y.pos.x()<=0||y.pos.y()<=0)
            {
                projectiles.remove(x);
            }

            if(!x.equals(managerID)&&y.intersects(pos))
            {
                try {
                    removeProjectile(x);
                    gp.player.reduceLives();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }


            }

        });
    }

    @Override
    public void receiveProjectile(ProjectileRecord pr) throws RemoteException {
        projectiles.put(pr.source(), new Projectile(pr));
        SoundManager.playSound("attack");
    }

    public void newProjectile( Point pos, String direction) throws RemoteException {
        ProjectileRecord pr = new ProjectileRecord(pos, direction, randName(), managerID);
        exchange.forwardState(managerID, pr);
        receiveProjectile(pr);
    }

    @Override
    public void remove(String source) {
        projectiles.remove(source);
        SoundManager.playSound("oi");
    }


    public void removeProjectile(String source) throws RemoteException {
        exchange.removeProjectile(source);

    }

    public static class Projectile{

        BufferedImage image;
        Point pos;
        String direction;
        int velocity = 320/45;
        int ttl = 90;
        String name;
        String source;

        public Projectile( Point pos, String source, String direction){
            new AffineTransform();
            this.name = randName();
            this.pos = pos;
            this.source = source;
            this.direction = direction;
            loadImage();
        }

        public Projectile(ProjectileRecord pr){
            this.pos = pr.pos();
            this.direction = pr.direction();
            this.name = pr.love();
            this.source = pr.source();
            loadImage();
        }
        public boolean intersects(Point that) {
            return this.pos.intersects(that);
        }
        private void loadImage(){
            try {

                image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/"+name+".png")));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void update(){
            int x = pos.x(); int y = pos.y();
            switch(direction){
                case "up"->    y -= velocity;
                case "down"->  y += velocity;
                case "left"->  x -= velocity;
                case "right"-> x += velocity;
            }

            this.pos = new Point(x, y);

            count();
        }

        public void draw(Graphics2D g2){
            double rad = Math.toRadians(ttl*6);
            AffineTransform at = AffineTransform.getTranslateInstance(this.pos.x(), this.pos.y());
            at.rotate(rad, 16, 16);
            g2.drawImage(image, at, null);
            //g2.drawImage(image, at, 32,32);
        }

        public void count(){
            ttl--;
        }

    }
}
