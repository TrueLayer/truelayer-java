package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.net.URI;

@Builder
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StartAuthorizationFlowRequest {

    @JsonProperty("redirect")
    private Redirect redirect;

    @JsonProperty("provider_selection")
    private ProviderSelection providerSelection;

    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProviderSelection {
        //todo ask payments-api about this empty object
    }

    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Redirect {
        @JsonProperty("return_uri")
        private URI returnUri;
    }
}
