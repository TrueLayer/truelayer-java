package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZATION_REQUIRED;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthorizationRequiredPaymentDetail extends PaymentDetail {

    private final Status status = AUTHORIZATION_REQUIRED;
}
