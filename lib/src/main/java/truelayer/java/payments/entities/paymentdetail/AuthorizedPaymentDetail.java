package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZED;

import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class AuthorizedPaymentDetail extends PaymentDetail {

    protected final Status status = AUTHORIZED;

    private Optional<AuthorizationFlowWithConfiguration> authorizationFlow;
}
