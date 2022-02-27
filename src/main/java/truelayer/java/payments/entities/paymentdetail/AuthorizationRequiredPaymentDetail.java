package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZATION_REQUIRED;

import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationRequiredPaymentDetail extends PaymentDetail {

    Status status = AUTHORIZATION_REQUIRED;
}
