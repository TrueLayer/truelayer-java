package truelayer.java.payments.entities.paymentmethod.provider;

import static truelayer.java.payments.entities.paymentmethod.provider.Provider.Type.USER_SELECTION;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSelectionProvider extends Provider {
    private final Type type = USER_SELECTION;

    private ProviderFilter filter;
}
