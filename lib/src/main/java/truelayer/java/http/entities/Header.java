package truelayer.java.http.entities;

import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Getter
@Accessors(fluent = true)
public class Header {
    String name;
    String value;
}
