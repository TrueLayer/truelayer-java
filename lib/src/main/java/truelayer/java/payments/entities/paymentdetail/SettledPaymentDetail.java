package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;

import java.util.Date;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SettledPaymentDetail extends PaymentDetail {

    private final Status status = SETTLED;

    private SourceOfFunds sourceOfFunds;

    private Date succeededAt;

    private Date settledAt;

    private Optional<AuthorizationFlow> authorizationFlow;
}
