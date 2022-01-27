package truelayer.java.auth.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessToken {
    String accessToken;

    int expiresIn;

    String scope;

    String tokenType;
}
