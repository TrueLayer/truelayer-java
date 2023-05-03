package com.truelayer.java;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Class that represent a custom proxy configuration
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public class ProxyConfiguration {

    /**
     * Hostname of the proxy
     */
    String hostname;

    /**
     * Port of the proxy
     */
    int port;

    /**
     * Optional credentials for authenticating proxy requests
     */
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
