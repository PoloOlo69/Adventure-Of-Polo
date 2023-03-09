package client;

import client.entity.Player;
import shared.records.Point;

@SuppressWarnings("ReassignedVariable")
public class CollisionChecker {



    public CollisionChecker() {}

    public static void checkTile(Player player) {
        // EXAMPLE CASE UP:           
        //              T1        T2
        //           ________  ________
        //          |        ||        |
        //          |        ||        |
        //          |________||________|
        //        
        //      yMin,xMin ________ xMax
        //               |        |
        //               | ENTITY |
        //               |________|
        //            yMax          
        //           

        // GET THE POSITION OF ENTITY SOLID AREA
        int xMin = player.pos.x()  + player.hitbox.xMin();
        int xMax = player.pos.x()  + player.hitbox.xSum();
        int yMin = player.pos.y()  + player.hitbox.yMin();
        int yMax = player.pos.y()  + player.hitbox.ySum();

        // DIVIDE BY TILE_SIZE TO GET THE INDEX OF THE POSITION THE SOLID AREA IS CURRENTLY AT IN THE MAP GRID
        int left = xMin / GamePanel.TILE_SIZE;
        int right = xMax / GamePanel.TILE_SIZE;
        int top = yMin / GamePanel.TILE_SIZE;
        int bottom = yMax / GamePanel.TILE_SIZE;

        // TILE INDEX FOR CHECKING IF THE NEXT TILE LEFT OR RIGHT OF THE HITBOX HAS COLLISION TURNED ON
        int t1, t2;

        switch (player.direction) {
            case "up" -> {

                // SUBTRACT PLAYER SPEED TO CHECK THE NEXT STEP IN ORDER TO PREVENT STICKING TO OBJECTS
                top = (yMin - player.velocity) / GamePanel.TILE_SIZE;
                if (yMin - player.velocity <= 1) {
                    player.collision = true;
                    break;

                }
                t1 = TileManager.getMap()[left][top];
                t2 = TileManager.getMap()[right][top];
                if (TileManager.tile[t1].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(left, top);
                    player.collected = new Point(left, top);

                }
                if (TileManager.tile[t2].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(right, top);
                    player.collected = new Point(right, top);
                }
                if (TileManager.tile[t1].collision || TileManager.tile[t2].collision) {
                    player.collision = true;
                }
            }
            case "down" -> {
                bottom = (yMax + player.velocity) / GamePanel.TILE_SIZE;
                if (yMax + player.velocity >= GamePanel.MAX_HEIGHT - 1) {
                    player.collision = true;
                    break;

                }
                t1 = TileManager.getMap()[left][bottom];
                t2 = TileManager.getMap()[right][bottom];
                if (TileManager.tile[t1].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(left, bottom);
                    player.collected = new Point(left, bottom);

                }
                if (TileManager.tile[t2].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(right, bottom);
                    player.collected = new Point(right, bottom);

                }
                if (TileManager.tile[t1].collision || TileManager.tile[t2].collision) {
                    player.collision = true;
                }
            }
            case "left" -> {
                left = (xMin - player.velocity) / GamePanel.TILE_SIZE;
                if (xMin - player.velocity <= 1) {
                    player.collision = true;
                    break;
                }
                t2 = TileManager.getMap()[left][top];
                t1 = TileManager.getMap()[left][bottom];
                if (TileManager.tile[t1].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(left, top);
                    player.collected = new Point(left, top);

                }
                if (TileManager.tile[t2].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(left, bottom);
                    player.collected = new Point(left, bottom);

                }
                if (TileManager.tile[t1].collision || TileManager.tile[t2].collision) {
                    player.collision = true;
                }
            }
            case "right" -> {
                right = (xMax + player.velocity) / GamePanel.TILE_SIZE;
                if (xMax + player.velocity >= GamePanel.MAX_WIDTH - 1) {
                    player.collision = true;
                    break;
                }
                t1 = TileManager.getMap()[right][top];
                t2 = TileManager.getMap()[right][bottom];
                if (TileManager.tile[t1].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(right, top);
                    player.collected = new Point(right, top);

                }
                if (TileManager.tile[t2].collectable) {
                    SoundManager.playSound("yeah");
                    TileManager.removeTile(right, bottom);
                    player.collected = new Point(right, bottom);
                }
                if (TileManager.tile[t1].collision || TileManager.tile[t2].collision) {
                    player.collision = true;
                }
            }
        }
    }
}