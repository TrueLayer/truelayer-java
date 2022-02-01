package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction.Type.PROVIDER_SELECTION;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ProviderSelection extends AuthorizationFlowAction {
    Type type = PROVIDER_SELECTION;

    List<Provider> providers;
}
