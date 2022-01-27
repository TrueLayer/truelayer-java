package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZED;

import com.fasterxml.jackson.annotation.JsonGetter;
import java.util.Optional;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizedPaymentDetail extends PaymentDetail {

    Status status = AUTHORIZED;

    AuthorizationFlowWithConfiguration authorizationFlow;

    @JsonGetter
    public Optional<AuthorizationFlowWithConfiguration> getAuthorizationFlow() {
        return Optional.ofNullable(authorizationFlow);
    }
}
