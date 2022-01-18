package truelayer.java.payments.entities.paymentdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZATION_REQUIRED;

@NoArgsConstructor
@Getter
public class AuthorizationRequiredPaymentDetail extends BasePaymentDetail {

    private final Status status = AUTHORIZATION_REQUIRED;
}
