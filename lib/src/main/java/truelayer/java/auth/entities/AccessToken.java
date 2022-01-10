package truelayer.java.auth.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class AccessToken {
    @JsonProperty("access_token")
    String accessToken;
    @JsonProperty("expires_in")
    int expiresIn;
    @JsonProperty("scope")
    String scope;
    @JsonProperty("token_type")
    String tokenType;
}
