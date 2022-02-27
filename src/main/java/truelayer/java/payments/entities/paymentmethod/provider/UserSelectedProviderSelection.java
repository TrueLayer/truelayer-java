package truelayer.java.payments.entities.paymentmethod.provider;

import static truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection.Type.USER_SELECTED;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSelectedProviderSelection extends ProviderSelection {
    private final Type type = USER_SELECTED;

    private ProviderFilter filter;
}
