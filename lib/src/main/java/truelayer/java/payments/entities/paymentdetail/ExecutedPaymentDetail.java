package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.EXECUTED;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Date;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPaymentDetail extends PaymentDetail {
    Status status = EXECUTED;

    PaymentSource paymentSource;

    Date executedAt;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }
}
