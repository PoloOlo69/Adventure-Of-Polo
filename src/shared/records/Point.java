package shared.records;

import java.io.Serializable;

public record Point(int x, int y) implements Serializable {
    public Point{n++;}
    public static long n;
    public static long pointsCreated(){return n;}

    public boolean intersects(Point that){
        return
                x() + 32 > that.x() &&
                y() + 32 > that.y() &&
                x() < that.x() + 64 &&
                y() < that.y() + 64;
    }
}
