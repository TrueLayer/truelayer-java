package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;

public class Authentication implements IAuthentication{

    @Override
    public AccessToken getOauthToken(String grantType, String scope, String clientId, String clientSecret) {
        //todo implement
        return null;
    }
}
