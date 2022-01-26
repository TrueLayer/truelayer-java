package truelayer.java.payments.entities;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
public class StartAuthorizationFlowRequest {

    private ProviderSelection providerSelection;

    private Redirect redirect;

    @ToString
    @EqualsAndHashCode
    @JsonInclude(Include.NON_NULL)
    public static class ProviderSelection {}

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(Include.NON_NULL)
    public static class Redirect {
        String returnUri;
    }

    @SuppressWarnings("unused")
    public static class StartAuthorizationFlowRequestBuilder {
        public StartAuthorizationFlowRequestBuilder withProviderSelection() {
            this.providerSelection = new ProviderSelection();
            return this;
        }
    }
}
