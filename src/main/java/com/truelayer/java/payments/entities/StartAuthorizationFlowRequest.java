package com.truelayer.java.payments.entities;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import java.net.URI;
import java.util.List;
import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class StartAuthorizationFlowRequest {

    private final ProviderSelection providerSelection;

    private final Redirect redirect;

    private final Consent consent;

    private final Form form;

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

        URI directReturnUri;
    }

    @Builder
    @ToString
    @EqualsAndHashCode
    @JsonInclude(Include.NON_ABSENT) // override global behaviour: this has to be always present if initialized
    public static class Consent {}

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Form {
        List<Input.Type> inputTypes;
    }

    public static StartAuthorizationFlowRequestBuilder builder() {
        return new StartAuthorizationFlowRequestBuilder();
    }

    public static class StartAuthorizationFlowRequestBuilder {
        private boolean withProviderSelection;

        private Redirect redirect;

        private Consent consent;

        private Form form;

        public StartAuthorizationFlowRequestBuilder withProviderSelection() {
            this.withProviderSelection = true;
            return this;
        }

        public StartAuthorizationFlowRequestBuilder redirect(Redirect redirect) {
            this.redirect = redirect;
            return this;
        }

        public StartAuthorizationFlowRequestBuilder consent(Consent consent) {
            this.consent = consent;
            return this;
        }

        public StartAuthorizationFlowRequestBuilder form(Form form) {
            this.form = form;
            return this;
        }

        public StartAuthorizationFlowRequest build() {
            return new StartAuthorizationFlowRequest(
                    withProviderSelection ? new ProviderSelection() : null, redirect, consent, form);
        }
    }
}
