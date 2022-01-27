package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.EXECUTED;

import java.util.Date;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPaymentDetail extends PaymentDetail {
    Status status = EXECUTED;

    SourceOfFunds sourceOfFunds;

    Date executedAt;

    Optional<AuthorizationFlowWithConfiguration> authorizationFlow;
}
