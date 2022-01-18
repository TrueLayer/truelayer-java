package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;
import static truelayer.java.payments.entities.paymentdetail.Status.SUCCEEDED;

@NoArgsConstructor
@Getter
public class SucceededPaymentDetail extends BasePaymentDetail {
    BaseSourceOfFunds sourceOfFunds;

    String succeededAt;

    Optional<AuthorizationFlow> authorizationFlow;

    private final Status status = SUCCEEDED;

    public SucceededPaymentDetail(
            String id,
            int amountInMinor,
            String currency,
            BaseBeneficiary beneficiary,
            User user,
            BasePaymentMethod paymentMethod,
            String createdAt,
            BaseSourceOfFunds sourceOfFunds,
            String succeededAt,
            AuthorizationFlow authorizationFlow
    ) {
        super(id, amountInMinor, currency, beneficiary, user, paymentMethod, createdAt);
        this.sourceOfFunds = sourceOfFunds;
        this.succeededAt = succeededAt;
        this.authorizationFlow = Optional.ofNullable(authorizationFlow);
    }
}
