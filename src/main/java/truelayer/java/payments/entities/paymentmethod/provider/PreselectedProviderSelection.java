package truelayer.java.payments.entities.paymentmethod.provider;

import static truelayer.java.payments.entities.paymentmethod.provider.ProviderSelection.Type.PRESELECTED;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.entities.Remitter;
import truelayer.java.payments.entities.SchemeId;

@Builder
@Getter
public class PreselectedProviderSelection extends ProviderSelection {
    private final Type type = PRESELECTED;

    private String providerId;

    private SchemeId schemeId;

    private Remitter remitter;
}
