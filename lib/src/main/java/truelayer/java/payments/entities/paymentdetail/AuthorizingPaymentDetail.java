package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;

@NoArgsConstructor
@Getter
public class AuthorizingPaymentDetail extends BasePaymentDetail {
    private final Status status = AUTHORIZING;
    private AuthorizationFlow authorizationFlow;

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
