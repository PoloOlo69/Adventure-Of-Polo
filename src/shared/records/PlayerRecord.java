package shared.records;

import java.io.Serializable;

public record PlayerRecord(String name, String direction, boolean moving, Point pos, Point collected, int spriteCounter, int lives) implements Serializable {
    public PlayerRecord{n++;}
    public static int n;
    public static int recordsSent(){return n;}
}