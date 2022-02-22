package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Date;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.entities.PaymentSource;

@Value
@EqualsAndHashCode(callSuper = false)
public class SettledPaymentDetail extends PaymentDetail {

    Status status = SETTLED;

    PaymentSource paymentSource;

    Date succeededAt;

    Date settledAt;

    Date executedAt;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }
}
