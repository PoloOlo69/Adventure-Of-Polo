package client;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;


import javax.imageio.ImageIO;

import shared.records.Point;


public class TileManager {

    public static Tile[] tile;

    public static int[][] map;

    public static int[][] getMap() {return map;}

    public TileManager(GamePanel gp) {

        TileManager.tile = new Tile[15];

        TileManager.map = new int[GamePanel.MAX_HORIZONTAL_TILES][GamePanel.MAX_VERTICAL_TILES];

        getTileImage();

        loadMap("01");

    }

    public void getTileImage() {

        System.out.print("Loading tiles... ");

        try {

            tile[0] = new Tile();
            tile[0].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/wall.jpg")));
            tile[0].collision = false;
            tile[0].png = false;

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/tree.png")));
            tile[1].collision = true;
            tile[1].png = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/wet.png")));
            tile[2].collision = true;
            tile[2].collectable = true;
            tile[2].png = true;

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/ocean1.png")));
            
            
            tile[5] = new Tile();
            tile[5].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/ocean2.png")));
            tile[5].collision = true;
            
            tile[6] = new Tile();
            tile[6].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/ocean3.png")));
            tile[6].collision = true;
            
            tile[7] = new Tile();
            tile[7].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/client/res/bridge.png")));
            tile[7].png = true;
            tile[7].index = 7;
            System.out.print("finished!");
        } catch (IOException e) {
            System.out.println("OOPS LOOKS LIKE SOMETHING WENT WRONG LOADING THE TILES...");
            e.printStackTrace();
        }
    }
    
    public void loadMap(String world) {

        try {

            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/client/maps/" +world+".txt"));

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            for (int i = 0; i < GamePanel.MAX_VERTICAL_TILES; i++) {

                String line = br.readLine();

                for (int j = 0; j < GamePanel.MAX_HORIZONTAL_TILES; j++) {

                    String[] number = line.split(" ");

                    int mapindex = Integer.parseInt(number[j]);

                    map[j][i] = mapindex;

                }

            }

            br.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void drawMap(Graphics2D g2) {

        for (int i = 0; i < GamePanel.MAX_VERTICAL_TILES; i++) {

            for (int j = 0; j < GamePanel.MAX_HORIZONTAL_TILES; j++) {

                int tileid = map[j][i];
                if(tileid!=0){
                    g2.drawImage(tile[0].image, j * GamePanel.TILE_SIZE, i * GamePanel.TILE_SIZE, GamePanel.TILE_SIZE,GamePanel.TILE_SIZE, null);
                } 

                g2.drawImage(tile[tileid].image, j * GamePanel.TILE_SIZE, i * GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);

            }

        }

    }

    public static void removeTile(int x, int y){
        if(map[x][y] != 0)
        {
            map[x][y] = 0;
        }
    }  
      
    public static void removeTile(Point pos){
        if(map[pos.x()][pos.y()] != 0)
        {
            map[pos.x()][pos.y()] = 0;
        }
    }

    public void draw(Graphics2D g2) {

        drawMap(g2);

    }

    public static class Tile {
        public BufferedImage image;

        public boolean collision = false;
        public boolean collectable = false;
        public boolean png = false;
        public int index;
    }
}
