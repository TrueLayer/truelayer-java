package truelayer.java.auth;

import java.io.IOException;
import java.util.List;
import lombok.Builder;
import truelayer.java.ClientCredentials;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

@Builder
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationApi authenticationApi;
    private final ClientCredentials clientCredentials;

    @Override
    public ApiResponse<AccessToken> getOauthToken(List<String> scopes) {
        try {
            return (ApiResponse<AccessToken>) authenticationApi
                    .getOauthToken(
                            clientCredentials.getClientId(),
                            clientCredentials.getClientSecret(),
                            ClientCredentials.GRANT_TYPE,
                            scopes)
                    .execute()
                    .body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get oauth token", e);
        }
    }
}
