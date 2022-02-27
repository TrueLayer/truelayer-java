package truelayer.java.payments.entities;

import static truelayer.java.payments.entities.paymentdetail.Status.AUTHORIZING;

import lombok.Value;
import truelayer.java.payments.entities.paymentdetail.Status;

@Value
public class StartAuthorizationFlowResponse {

    AuthorizationFlow authorizationFlow;

    Status status = AUTHORIZING;
}
