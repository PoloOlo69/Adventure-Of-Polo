package shared.records;

import java.io.Serializable;

public record ProjectileRecord( Point pos, String direction, String love, String source) implements Serializable {
    public ProjectileRecord{n++;}
    public static long n;
    public static long recordsSent(){return n;}
}
