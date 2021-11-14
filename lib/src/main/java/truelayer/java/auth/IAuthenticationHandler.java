package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.io.IOException;
import java.util.List;

public interface IAuthenticationHandler {
    AccessToken getOauthToken(List<String> scopes) throws AuthenticationException, IOException;
}
