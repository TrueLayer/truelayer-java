package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction.Type.WAIT;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class WaitForOutcome extends AuthorizationFlowAction {
    Type type = WAIT;
}
