package truelayer.java.auth;

import lombok.Builder;
import lombok.Getter;
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
import java.util.function.Consumer;

@Builder
@Getter
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationApi authenticationApi;
    private final ClientCredentialsOptions clientCredentialsOptions;

    @Override
    public ApiResponse<AccessToken> getOauthToken(List<String> scopes) {
        try {
            return (ApiResponse<AccessToken>) authenticationApi.getOauthToken(clientCredentialsOptions.getClientId(),
                    clientCredentialsOptions.getClientSecret(),
                    ClientCredentialsOptions.GRANT_TYPE,
                    scopes).execute().body();
        } catch (IOException e) {
            throw new TrueLayerException("unable to get oauth token", e);
        }

    }

    @Override
    public ICancellableTask getOauthToken(List<String> scopes, IApiCallback<ApiResponse<AccessToken>> callback) {
        var call = authenticationApi.getOauthToken(clientCredentialsOptions.getClientId(),
                clientCredentialsOptions.getClientSecret(),
                ClientCredentialsOptions.GRANT_TYPE,
                scopes);

        call.enqueue(new Callback<ApiResponse<AccessToken>>() {
            @Override
            public void onResponse(Call<ApiResponse<AccessToken>> call, Response<ApiResponse<AccessToken>> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<ApiResponse<AccessToken>> call, Throwable t) {
                throw new TrueLayerException(t);
            }
        });

        return new CancellableTask(call);
    }
}
