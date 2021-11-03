package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

public interface IAuthentication {
    AccessToken getOauthToken(String grantType, String scope, String clientId, String clientSecret) throws AuthenticationException;
}
