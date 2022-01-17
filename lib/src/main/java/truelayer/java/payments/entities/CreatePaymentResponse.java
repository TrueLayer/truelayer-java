package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("user")
    private User user;

    @JsonProperty("payment_token")
    private String paymentToken;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        @JsonProperty("id")
        private String id;
    }
}
