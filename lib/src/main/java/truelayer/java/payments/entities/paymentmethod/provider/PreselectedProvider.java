package truelayer.java.payments.entities.paymentmethod.provider;

import static truelayer.java.payments.entities.paymentmethod.provider.Provider.Type.PRESELECTED;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.SchemeId;
import truelayer.java.payments.entities.paymentmethod.Remitter;

@Builder
@Getter
public class PreselectedProvider extends Provider {
    private final Type type = PRESELECTED;

    private String providerId;

    private SchemeId schemeId;

    private Remitter remitter;
}
