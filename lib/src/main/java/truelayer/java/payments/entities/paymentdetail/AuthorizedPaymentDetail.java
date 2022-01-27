package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZED;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedPaymentDetail extends PaymentDetail {

    Status status = AUTHORIZED;

    Optional<AuthorizationFlowWithConfiguration> authorizationFlow;
}
