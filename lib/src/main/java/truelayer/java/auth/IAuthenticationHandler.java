package truelayer.java.auth;

import java.util.List;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

public interface IAuthenticationHandler {
    ApiResponse<AccessToken> getOauthToken(List<String> scopes);
}
