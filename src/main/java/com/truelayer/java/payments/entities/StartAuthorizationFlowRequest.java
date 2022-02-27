package com.truelayer.java.payments.entities;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.net.URI;
import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class StartAuthorizationFlowRequest {

    private final ProviderSelection providerSelection;

    private final Redirect redirect;

    @ToString
    @EqualsAndHashCode
    @JsonInclude(Include.NON_ABSENT) // override global behaviour: this has to be always present if initialized
    public static class ProviderSelection {}

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Redirect {
        URI returnUri;
    }

    public static StartAuthorizationFlowRequestBuilder builder() {
        return new StartAuthorizationFlowRequestBuilder();
    }

    public static class StartAuthorizationFlowRequestBuilder {
        private boolean withProviderSelection;

        private Redirect redirect;

        public StartAuthorizationFlowRequestBuilder withProviderSelection() {
            this.withProviderSelection = true;
            return this;
        }

        public StartAuthorizationFlowRequestBuilder redirect(Redirect redirect) {
            this.redirect = redirect;
            return this;
        }

        public StartAuthorizationFlowRequest build() {
            return new StartAuthorizationFlowRequest(withProviderSelection ? new ProviderSelection() : null, redirect);
        }
    }
}
