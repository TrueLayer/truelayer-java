package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZATION_REQUIRED;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
public class AuthorizationRequiredPaymentDetail extends BasePaymentDetail {

    public AuthorizationRequiredPaymentDetail(
            String id,
            int amountInMinor,
            String currency,
            BaseBeneficiary beneficiary,
            User user,
            BasePaymentMethod paymentMethod,
            String createdAt
    ) {
        super(id, amountInMinor, currency, beneficiary, user, paymentMethod, createdAt, AUTHORIZATION_REQUIRED);
    }

}
