package truelayer.java.payments.entities.paymentdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;

@NoArgsConstructor
@Getter
public class SettledPaymentDetail extends BasePaymentDetail {

    private final Status status = SETTLED;
    BaseSourceOfFunds sourceOfFunds;
    String succeededAt;
    String settledAt;
    Optional<AuthorizationFlow> authorizationFlow;
}
