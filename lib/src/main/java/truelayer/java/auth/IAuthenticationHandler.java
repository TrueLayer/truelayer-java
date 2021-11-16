package truelayer.java.auth;

import truelayer.java.auth.entities.AccessToken;

import java.io.IOException;
import java.util.List;

public interface IAuthenticationHandler {
    AccessToken getOauthToken(List<String> scopes) throws IOException;
}
