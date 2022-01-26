package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthorizingPaymentDetail extends PaymentDetail {
    private final Status status = AUTHORIZING;

    private AuthorizationFlowWithConfiguration authorizationFlow;
}
