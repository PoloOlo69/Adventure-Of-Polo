package shared.records;

import java.io.Serializable;

public record Wrapper<T>(T record) implements Serializable {
}
