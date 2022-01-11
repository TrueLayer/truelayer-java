package truelayer.java.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import truelayer.java.ClientCredentialsOptions;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.CancellableTask;
import truelayer.java.http.IApiCallback;
import truelayer.java.http.ICancellableTask;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Builder
@Getter
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationApi authenticationApi;
    private final ClientCredentialsOptions clientCredentialsOptions;

    @Override
    @SneakyThrows
    public CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes) {
        var response =  authenticationApi.getOauthToken(clientCredentialsOptions.getClientId(),
                clientCredentialsOptions.getClientSecret(),
                ClientCredentialsOptions.GRANT_TYPE,
                scopes);
        return response;
    }
}
