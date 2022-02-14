package truelayer.java.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import lombok.Value;
import truelayer.java.ClientCredentials;
import truelayer.java.Environment;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.versioninfo.VersionInfo;
import truelayer.java.http.entities.ApiResponse;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * @inheritDoc
 */
@Value
public class AuthenticationHandler implements IAuthenticationHandler {
    VersionInfo versionInfo;

    Environment environment;

    ClientCredentials clientCredentials;

    IAuthenticationApi authenticationApi;

    public static AuthenticationHandlerBuilder New() {
        return new AuthenticationHandlerBuilder();
    }

    @Override
    public CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes) {
        return authenticationApi.getOauthToken(
                clientCredentials.clientId(), clientCredentials.clientSecret(), ClientCredentials.GRANT_TYPE, scopes);
    }
}
