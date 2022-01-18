package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderSelection extends BaseAuthorizationFlowAction {
    private String type = "provider_selection";

    private List<Provider> providers;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Provider {
        private String providerId;

        private String displayName;

        private String iconUri;

        private String logoUri;

        private String bgColor;

        private String countryCode;
    }
}
