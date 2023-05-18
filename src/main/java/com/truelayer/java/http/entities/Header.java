package com.truelayer.java.http.entities;

import lombok.Getter;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Getter
@Accessors(fluent = true)
public class Header { // TODO: review this class including its name
    String name;
    String value;

    @Override
    public String toString() {
        return this.name + "=" + value;
    }
}
