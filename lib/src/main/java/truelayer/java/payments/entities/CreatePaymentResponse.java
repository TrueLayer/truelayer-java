package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentResponse {
    private String id;

    private User user;

    private String paymentToken;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class User {
        private String id;
    }
}
