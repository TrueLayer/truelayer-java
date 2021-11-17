package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;

import java.util.List;

public interface IAuthenticationHandler {
    AccessToken getOauthToken(List<String> scopes);
}
