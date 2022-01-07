package truelayer.java.auth;

import retrofit2.Callback;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.IApiCallback;
import truelayer.java.http.ICancellableTask;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;

import java.util.List;
import java.util.function.Consumer;

public interface IAuthenticationHandler {
    ApiResponse<AccessToken> getOauthToken(List<String> scopes);

    ICancellableTask getOauthToken(List<String> scopes, IApiCallback<ApiResponse<AccessToken>> onResponse);
}
