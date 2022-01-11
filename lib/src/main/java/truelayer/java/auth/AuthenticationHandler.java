package truelayer.java.auth;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.ClientCredentials;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

import java.io.IOException;
import java.util.List;

@Builder
@Getter
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationApi authenticationApi;
    private final ClientCredentials clientCredentials;

    @Override
    public ApiResponse<AccessToken> getOauthToken(List<String> scopes) {
        try {
            return (ApiResponse<AccessToken>) authenticationApi.getOauthToken(clientCredentials.getClientId(),
                    clientCredentials.getClientSecret(),
                    ClientCredentials.GRANT_TYPE,
                    scopes).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get oauth token", e);
        }

    }
}
