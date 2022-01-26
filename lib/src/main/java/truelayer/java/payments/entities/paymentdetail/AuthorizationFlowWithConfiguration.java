package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.payments.entities.AuthorizationFlow;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationFlowWithConfiguration extends AuthorizationFlow {

    Configuration configuration;
}
