package truelayer.java.payments.entities.paymentmethod.provider;

import static truelayer.java.payments.entities.paymentmethod.provider.Provider.Type.PRESELECTION;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.SchemeId;
import truelayer.java.payments.entities.paymentmethod.Remitter;

@Builder
@Getter
public class PreselectionProvider extends Provider {
    private final Type type = PRESELECTION;

    private String providerId;

    private SchemeId schemeId;

    private Remitter remitter;
}
