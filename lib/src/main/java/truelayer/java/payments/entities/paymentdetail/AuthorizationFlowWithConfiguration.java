package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.payments.entities.AuthorizationFlow;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationFlowWithConfiguration extends AuthorizationFlow {

    Configuration configuration;
}
