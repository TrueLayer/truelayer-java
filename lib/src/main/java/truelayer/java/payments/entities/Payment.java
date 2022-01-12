package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment {
    @JsonProperty("id")
    private String id;

    @JsonProperty("payment_token")
    private String paymentToken;

    @JsonProperty("user")
    private User user;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        @JsonProperty("id")
        private String id;
    }
}
