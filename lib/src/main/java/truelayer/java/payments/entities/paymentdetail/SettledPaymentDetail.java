package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.SETTLED;

@NoArgsConstructor
@Getter
public class SettledPaymentDetail extends BasePaymentDetail {

    BaseSourceOfFunds sourceOfFunds;

    String succeededAt;

    String settledAt;

    Optional<AuthorizationFlow> authorizationFlow;

    private final Status status = SETTLED;

    public SettledPaymentDetail(
            String id,
            int amountInMinor,
            String currency,
            BaseBeneficiary beneficiary,
            User user,
            BasePaymentMethod paymentMethod,
            String createdAt,
            BaseSourceOfFunds sourceOfFunds,
            String succeededAt,
            String settledAt,
            AuthorizationFlow authorizationFlow
    ) {
        super(id, amountInMinor, currency, beneficiary, user, paymentMethod, createdAt);
        this.sourceOfFunds = sourceOfFunds;
        this.settledAt = settledAt;
        this.succeededAt = succeededAt;
        this.authorizationFlow = Optional.ofNullable(authorizationFlow);
    }
}
