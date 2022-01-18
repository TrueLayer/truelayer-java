package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
public class AuthorizingPaymentDetail extends BasePaymentDetail {
    AuthorizationFlow authorizationFlow;

    public AuthorizingPaymentDetail(
            String id,
            int amountInMinor,
            String currency,
            BaseBeneficiary beneficiary,
            User user,
            BasePaymentMethod paymentMethod,
            String createdAt,
            AuthorizationFlow authorizationFlow
    ) {
        super(id, amountInMinor, currency, beneficiary, user, paymentMethod, createdAt, AUTHORIZING);
        this.authorizationFlow = authorizationFlow;
    }

    @Getter
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthorizationFlow {

        Actions actions;

        Configuration configuration;

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Actions {

            private BaseAuthorizationFlowAction next;
        }
    }
}
