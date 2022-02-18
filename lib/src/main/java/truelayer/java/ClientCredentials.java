package truelayer.java;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Class that models TrueLayer client credentials required for Oauth2 protected endpoints.
 * It should be built with the help of its builder class.
 *
 * @see ClientCredentialsBuilder
 */
@Getter
@EqualsAndHashCode
@ToString
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientCredentials {
    String clientId;

    String clientSecret;

    public static String GRANT_TYPE = "client_credentials";

    public static ClientIdStep builder() {
        return new ClientCredentialsBuilder();
    }

    public static class ClientCredentialsBuilder implements ClientIdStep, ClientSecretStep, BuildStep {

        private ClientCredentials clientCredentials;

        public ClientCredentialsBuilder() {
            clientCredentials = new ClientCredentials();
        }

        public ClientSecretStep clientId(String clientId) {
            if (ObjectUtils.isEmpty(clientId)) {
                throw new TrueLayerException("client id must be set");
            }
            clientCredentials.clientId = clientId;
            return this;
        }

        @Override
        public BuildStep clientSecret(String clientSecret) {
            if (ObjectUtils.isEmpty(clientSecret)) {
                throw new TrueLayerException("client secret must be set");
            }
            clientCredentials.clientSecret = clientSecret;
            return this;
        }

        @Override
        public ClientCredentials build() {
            return clientCredentials;
        }
    }

    public interface ClientIdStep {
        ClientSecretStep clientId(String clientId);
    }

    public interface ClientSecretStep {
        BuildStep clientSecret(String clientSecret);
    }

    public interface BuildStep {
        ClientCredentials build();
    }
}
