package com.truelayer.java;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Builder
@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public class ProxyConfigurations {

    String hostname;

    int port;

    Credentials credentials;

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    @Accessors(fluent = true)
    public static class Credentials {
        String username;

        String password;
    }
}
