package truelayer.java.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

public interface IAuthenticationHandler {
    CompletableFuture<ApiResponse<AccessToken>> getOauthToken(List<String> scopes);
}
