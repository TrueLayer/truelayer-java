package truelayer.java.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import retrofit2.Call;
import truelayer.java.ClientCredentialsOptions;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.ApiResponse;

import java.util.List;

@Builder
@Getter
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationApi authenticationApi;
    private final ClientCredentialsOptions clientCredentialsOptions;

    @SneakyThrows
    @Override
    public ApiResponse<AccessToken> getOauthToken(List<String> scopes) {
        return (ApiResponse<AccessToken>) authenticationApi.getOauthToken(clientCredentialsOptions.getClientId(),
                clientCredentialsOptions.getClientSecret(),
                ClientCredentialsOptions.GRANT_TYPE,
                scopes).execute().body();

    }
}
