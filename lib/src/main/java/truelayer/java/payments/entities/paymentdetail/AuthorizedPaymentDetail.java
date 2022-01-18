package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import java.util.Optional;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZED;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
public class AuthorizedPaymentDetail extends BasePaymentDetail {

    Optional<AuthorizationFlow> authorizationFlow;

    public AuthorizedPaymentDetail(
            String id,
            int amountInMinor,
            String currency,
            BaseBeneficiary beneficiary,
            User user,
            BasePaymentMethod paymentMethod,
            String createdAt,
            AuthorizationFlow authorizationFlow
    ) {
        super(id, amountInMinor, currency, beneficiary, user, paymentMethod, createdAt, AUTHORIZED);
        this.authorizationFlow = Optional.ofNullable(authorizationFlow);
    }
}
