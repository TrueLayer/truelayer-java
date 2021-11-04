package truelayer.java.auth.entities;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class AccessToken {
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("expires_in")
    int expiresIn;
    @SerializedName("scope")
    String scope;
    @SerializedName("token_type")
    String tokenType;
}
