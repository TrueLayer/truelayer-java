package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreatePaymentResponse {
    String id;

    User user;

    String paymentToken;
}
