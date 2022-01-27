package truelayer.java.payments.entities.paymentmethod.provider;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSelectionProvider extends Provider {
    private final String type = "user_selection";

    private final ProviderFilter filter;
}
