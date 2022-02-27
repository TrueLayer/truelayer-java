package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction.Type.REDIRECT;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Redirect extends AuthorizationFlowAction {
    Type type = REDIRECT;

    URI uri;

    Metadata metadata;
}
