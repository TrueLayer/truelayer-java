package truelayer.java.payments.entities.paymentmethod.provider;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.SchemeId;
import truelayer.java.payments.entities.paymentmethod.Remitter;

@Builder
@Getter
@JsonPropertyOrder({"type", "provider_id", "scheme_id", "remitter"})
public class PreselectedProvider extends Provider {
    private final String type = "preselected";

    private final String providerId;

    private final SchemeId schemeId;

    private Remitter remitter;
}
