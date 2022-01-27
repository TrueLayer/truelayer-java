package truelayer.java.payments.entities.paymentdetail;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ProviderSelection extends AuthorizationFlowAction {
    String type = "provider_selection";

    List<Provider> providers;
}
