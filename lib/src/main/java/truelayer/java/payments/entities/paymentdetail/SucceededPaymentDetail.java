package truelayer.java.payments.entities.paymentdetail;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.SUCCEEDED;

@NoArgsConstructor
@Getter
public class SucceededPaymentDetail extends BasePaymentDetail {
    private final Status status = SUCCEEDED;

    private BaseSourceOfFunds sourceOfFunds;

    private Date succeededAt;

    private Optional<AuthorizationFlow> authorizationFlow;
}
