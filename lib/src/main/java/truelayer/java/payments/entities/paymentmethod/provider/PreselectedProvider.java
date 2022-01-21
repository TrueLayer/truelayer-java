package truelayer.java.payments.entities.paymentmethod.provider;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.SchemeId;
import truelayer.java.payments.entities.paymentmethod.Remitter;

@Builder
@Getter
public class PreselectedProvider extends BaseProvider {
    private final String type = "preselected";

    private final String providerId;

    private final SchemeId schemeId;

    private Remitter remitter;
}
