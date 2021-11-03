package truelayer.java.auth.entities;

public class AccessToken {
    private final String accessToken;

    private final int expiresIn;

    private final String scope;

    private final String tokenType;

    public AccessToken(String accessToken, int expiresIn, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }
}
