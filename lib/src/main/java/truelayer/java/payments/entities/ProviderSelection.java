package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderSelection extends BaseAuthorizationFlowAction {
    @JsonProperty("type")
    private String type = "provider_selection";

    @JsonProperty("providers")
    private List<Provider> providers;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Provider {
        @JsonProperty("provider_id")
        private String providerId;

        @JsonProperty("display_name")
        private String displayName;

        @JsonProperty("icon_uri")
        private String iconUri;

        @JsonProperty("logo_uri")
        private String logoUri;

        @JsonProperty("bg_color")
        private String bgColor;

        @JsonProperty("country_code")
        private String countryCode;
    }
}
