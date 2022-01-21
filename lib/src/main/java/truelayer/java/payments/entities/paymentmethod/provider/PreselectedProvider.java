package truelayer.java.payments.entities.paymentmethod.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import truelayer.java.payments.entities.SchemeId;
import truelayer.java.payments.entities.paymentmethod.Remitter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "provider_id", "scheme_id", "remitter"})
public class PreselectedProvider extends BaseProvider {
    private final String type = "preselected";

    private final String providerId;

    private final SchemeId schemeId;

    private Remitter remitter;
}
