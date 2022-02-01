package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction.Type.REDIRECT;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Redirect extends AuthorizationFlowAction {
    Type type = REDIRECT;

    String uri;

    Metadata metadata;
}
