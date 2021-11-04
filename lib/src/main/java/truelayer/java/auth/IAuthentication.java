package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.util.List;

public interface IAuthentication {
    AccessToken getOauthToken(List<String> scopes) throws AuthenticationException;
}
