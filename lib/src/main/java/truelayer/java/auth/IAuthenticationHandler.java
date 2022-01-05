package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

import java.util.List;

public interface IAuthenticationHandler {
    ApiResponse<AccessToken> getOauthToken(List<String> scopes);
}
