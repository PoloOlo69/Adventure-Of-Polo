package shared.records;

import java.io.Serializable;

public record Rectangle (int xMin,int xMax,int yMin,int yMax) implements Serializable {
    public Rectangle{n++;}
    public static int n;
    public static long rectanglesCreated(){return n;}
    public int xSum(){return xMin+xMax;}
    public int ySum(){return yMin+yMax;}
}
