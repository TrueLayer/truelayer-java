package truelayer.java;

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
public class ClientCredentials {
    String clientId;

    String clientSecret;

    public static String GRANT_TYPE = "client_credentials";
}
