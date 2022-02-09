package truelayer.java.auth.entities;

import lombok.Value;

/**
 * Model for access token
 */
@Value
public class AccessToken {
    String accessToken;

    int expiresIn;

    String scope;

    String tokenType;
}
