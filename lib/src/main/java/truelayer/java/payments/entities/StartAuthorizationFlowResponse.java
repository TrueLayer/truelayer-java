package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.Status;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

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
