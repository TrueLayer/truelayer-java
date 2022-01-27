package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;

import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettledPaymentDetail extends PaymentDetail {

    Status status = SETTLED;

    SourceOfFunds sourceOfFunds;

    Date succeededAt;

    Date settledAt;

    Date executedAt;

    Optional<AuthorizationFlowWithConfiguration> authorizationFlow;
}
