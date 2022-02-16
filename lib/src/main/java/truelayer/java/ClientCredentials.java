package truelayer.java;

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
    String clientId;

    String clientSecret;

    public static String GRANT_TYPE = "client_credentials";

    // todo add custom build with validation
}
