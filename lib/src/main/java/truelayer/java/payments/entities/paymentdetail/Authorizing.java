package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import truelayer.java.payments.entities.beneficiary.BaseBeneficiary;
import truelayer.java.payments.entities.paymentmethod.BasePaymentMethod;

import static truelayer.java.payments.entities.paymentdetail.BasePaymentDetail.Status.AUTHORIZING;

@Getter
@Value
@EqualsAndHashCode(callSuper = true)
public class Authorizing extends BasePaymentDetail {
    private String id;

    private int amountInMinor;

    private String currency;

    private BaseBeneficiary beneficiary;

    private User user;

    private BasePaymentMethod paymentMethod;

    private String createdAt;

    private AuthorizationFlow authorizationFlow;

    private final Status status = AUTHORIZING;

    @Getter
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthorizationFlow {

        private Actions actions;

        private BasePaymentDetail.AuthorizationFlow.Configuration configuration;

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Actions {

            private BaseAuthorizationFlowAction next;
        }
    }
}
