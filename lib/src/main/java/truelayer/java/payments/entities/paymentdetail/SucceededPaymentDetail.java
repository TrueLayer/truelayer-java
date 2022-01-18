package truelayer.java.payments.entities.paymentdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.SUCCEEDED;

@NoArgsConstructor
@Getter
public class SucceededPaymentDetail extends BasePaymentDetail {
    private final Status status = SUCCEEDED;
    BaseSourceOfFunds sourceOfFunds;
    String succeededAt;
    Optional<AuthorizationFlow> authorizationFlow;
}
