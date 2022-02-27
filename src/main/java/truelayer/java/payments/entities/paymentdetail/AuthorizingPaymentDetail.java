package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizingPaymentDetail extends PaymentDetail {

    Status status = AUTHORIZING;

    AuthorizationFlowWithConfiguration authorizationFlow;
}
