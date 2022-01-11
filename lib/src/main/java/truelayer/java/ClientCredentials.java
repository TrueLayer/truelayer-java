package truelayer.java;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientCredentials {
    private String clientId;

    private String clientSecret;

    public static String GRANT_TYPE = "client_credentials";
}
