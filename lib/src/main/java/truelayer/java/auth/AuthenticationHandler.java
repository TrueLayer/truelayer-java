package truelayer.java.auth;

import java.io.IOException;
import java.util.List;
import lombok.Value;
import truelayer.java.ClientCredentials;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

@Value
public class AuthenticationHandler implements IAuthenticationHandler {
    ClientCredentials clientCredentials;

    IAuthenticationApi authenticationApi;

    @Override
    public ApiResponse<AccessToken> getOauthToken(List<String> scopes) {
        try {
            return (ApiResponse<AccessToken>) authenticationApi
                    .getOauthToken(
                            clientCredentials.clientId(),
                            clientCredentials.clientSecret(),
                            ClientCredentials.GRANT_TYPE,
                            scopes)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get oauth token", e);
        }
    }
}
