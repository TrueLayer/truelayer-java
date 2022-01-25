package truelayer.java.payments.entities;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.Status;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StartAuthorizationFlowResponse {

    AuthorizationFlow authorizationFlow;

    Status status = AUTHORIZING;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthorizationFlow {
        Actions actions;
    }
}
