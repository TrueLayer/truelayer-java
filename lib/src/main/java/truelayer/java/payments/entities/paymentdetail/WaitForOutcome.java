package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class WaitForOutcome extends AuthorizationFlowAction {
    private final String type = "wait";
}
