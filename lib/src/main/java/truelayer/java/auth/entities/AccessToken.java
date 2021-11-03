package truelayer.java.auth.entities;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    @SerializedName("access_token")
    private final String accessToken;
    @SerializedName("expires_in")
    private final int expiresIn;
    @SerializedName("scope")
    private final String scope;
    @SerializedName("token_type")
    private final String tokenType;

    public AccessToken(String accessToken, int expiresIn, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.tokenType = tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }
}
