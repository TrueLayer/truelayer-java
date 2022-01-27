package truelayer.java.auth.entities;

import lombok.Value;

@Value
public class AccessToken {
    String accessToken;

    int expiresIn;

    String scope;

    String tokenType;
}
