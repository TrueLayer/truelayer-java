package com.truelayer.java;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;

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
    String clientId;

    String clientSecret;

    public static String GRANT_TYPE = "client_credentials";

    public static class ClientCredentialsBuilder {

        public ClientCredentials build() {
            if (ObjectUtils.isEmpty(this.clientId)) {
                throw new TrueLayerException("client id must be set");
            }
            if (ObjectUtils.isEmpty(this.clientSecret)) {
                throw new TrueLayerException("client secret must be set");
            }
            return new ClientCredentials(clientId, clientSecret);
        }
    }
}
