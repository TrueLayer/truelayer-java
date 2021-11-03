package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;

public interface IAuthentication {
    AccessToken getOauthToken(String grantType, String scope, String clientId, String clientSecret);
}
