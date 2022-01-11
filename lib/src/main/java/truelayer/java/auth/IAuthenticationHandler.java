package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IAuthenticationHandler {
    CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes);
}
