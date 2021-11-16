package truelayer.java;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientCredentialsOptions {
    private String clientId;

    private String clientSecret;

    public static String GRANT_TYPE = "client_credentials";
}
