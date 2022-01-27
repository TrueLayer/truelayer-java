package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZED;

import java.util.Optional;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizedPaymentDetail extends PaymentDetail {

    Status status = AUTHORIZED;

    Optional<AuthorizationFlowWithConfiguration> authorizationFlow;
}
