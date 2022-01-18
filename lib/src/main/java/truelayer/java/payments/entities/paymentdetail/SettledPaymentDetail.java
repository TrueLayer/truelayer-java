package truelayer.java.payments.entities.paymentdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;

@NoArgsConstructor
@Getter
public class SettledPaymentDetail extends BasePaymentDetail {

    private final Status status = SETTLED;

    private BaseSourceOfFunds sourceOfFunds;

    private Date succeededAt;

    private Date settledAt;

    private Optional<AuthorizationFlow> authorizationFlow;
}
