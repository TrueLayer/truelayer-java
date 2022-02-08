package truelayer.java.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Value;
import truelayer.java.ClientCredentials;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

/**
 * @inheritDoc
 */
@Value
public class AuthenticationHandler implements IAuthenticationHandler {
    ClientCredentials clientCredentials;

    IAuthenticationApi authenticationApi;

    @Override
    public CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes) {
        return authenticationApi.getOauthToken(
                clientCredentials.clientId(), clientCredentials.clientSecret(), ClientCredentials.GRANT_TYPE, scopes);
    }
}
