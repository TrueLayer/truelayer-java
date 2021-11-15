package truelayer.java.auth;

import lombok.Builder;
import truelayer.java.ClientCredentialsOptions;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.TrueLayerException;

import java.io.IOException;
import java.util.List;

@Builder
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationApi authenticationApi;
    private final ClientCredentialsOptions clientCredentialsOptions;

    @Override
    public AccessToken getOauthToken(List<String> scopes) throws IOException {
        return authenticationApi.getOauthToken(clientCredentialsOptions.getClientId(),
                        clientCredentialsOptions.getClientSecret(),
                        ClientCredentialsOptions.GRANT_TYPE,
                        scopes)
                .execute().body();
/*
        //todo extract to a more generic error handling logic
        if (oauthTokenResponse.isSuccessful()) {
            return oauthTokenResponse.body();
        }
        throw new AuthenticationException(oauthTokenResponse.errorBody().string());*/
    }
}
