package com.truelayer.java;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Class that models TrueLayer client credentials required for Oauth2 protected endpoints.
 * It should be built with the help of its builder class.
 *
 * @see ClientCredentialsBuilder
 */
@Builder
@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
public class ClientCredentials {

    @NotEmpty(message = "client id must be set")
    String clientId;

    @NotEmpty(message = "client secret must be set")
    String clientSecret;

    public static String GRANT_TYPE = "client_credentials";

    public static class ClientCredentialsBuilder {

        public ClientCredentials build() {
            ClientCredentials clientCredentials = new ClientCredentials(clientId, clientSecret);
            Utils.validateObject(clientCredentials);
            return clientCredentials;
        }
    }
}
