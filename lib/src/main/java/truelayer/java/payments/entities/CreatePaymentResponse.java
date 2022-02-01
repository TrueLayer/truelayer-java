package truelayer.java.payments.entities;

import lombok.Value;

@Value
public class CreatePaymentResponse {
    String id;

    User user;

    String paymentToken;
}
