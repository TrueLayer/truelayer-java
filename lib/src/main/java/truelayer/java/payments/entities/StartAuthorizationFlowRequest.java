package truelayer.java.payments.entities;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class StartAuthorizationFlowRequest {

    private ProviderSelection providerSelection;

    private Redirect redirect;

    @ToString
    @EqualsAndHashCode
    @JsonInclude(Include.NON_ABSENT) // override global behaviour: this has to be always present if initialized
    public static class ProviderSelection {}

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
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
