package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class StartAuthorizationFlowRequest {

    private final ProviderSelection providerSelection;

    private final Map<String, String> schemeSelection;

    private final Redirect redirect;

    private final Consent consent;

    private final Form form;

    private final Map<String, String> userAccountSelection;

    public static StartAuthorizationFlowRequestBuilder builder() {
        return new StartAuthorizationFlowRequestBuilder();
    }

    // TODO: remove in future major version, as we will no longer support empty provider selection
    public static class StartAuthorizationFlowRequestBuilder {
        private boolean withProviderSelection;

        private ProviderSelection providerSelection;

        private Map<String, String> schemeSelection;

        private Redirect redirect;

        private Consent consent;

        private Form form;

        private Map<String, String> userAccountSelection;

        /**
         * Include an empty provider selection object in the request
         * @deprecated use providerSelection(ProviderSelection) instead
         * @return the builder object
         */
        @Deprecated
        public StartAuthorizationFlowRequestBuilder withProviderSelection() {
            this.withProviderSelection = true;
            return this;
        }

        public StartAuthorizationFlowRequestBuilder providerSelection(ProviderSelection providerSelection) {
            this.providerSelection = providerSelection;
            return this;
        }

        public StartAuthorizationFlowRequestBuilder schemeSelection(Map<String, String> schemeSelection) {
            this.schemeSelection = schemeSelection;
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

        public StartAuthorizationFlowRequestBuilder userAccountSelection(Map<String, String> userAccountSelection) {
            this.userAccountSelection = userAccountSelection;
            return this;
        }

        public StartAuthorizationFlowRequest build() {
            if (withProviderSelection && providerSelection == null) {
                providerSelection = new ProviderSelection();
            }

            return new StartAuthorizationFlowRequest(
                    providerSelection, schemeSelection, redirect, consent, form, userAccountSelection);
        }
    }

    @Builder
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    public static class ProviderSelection {
        private Icon icon;

        public ProviderSelection() {}

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @EqualsAndHashCode
        @ToString
        public static class Icon {
            private IconType type;

            @RequiredArgsConstructor
            @Getter
            public enum IconType {
                DEFAULT("default"),
                EXTENDED("extended"),
                EXTENDED_SMALL("extended_small"),
                EXTENDED_MEDIUM("extended_medium"),
                EXTENDED_LARGE("extended_large");

                @JsonValue
                private final String type;
            }
        }
    }

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Redirect {
        private final URI returnUri;

        private final URI directReturnUri;
    }

    @Builder
    @ToString
    @EqualsAndHashCode
    @Getter
    public static class Consent {
        private final ActionType actionType;
        private final Requirements requirements;

        @RequiredArgsConstructor
        @Getter
        public enum ActionType {
            EXPLICIT("explicit"),
            ADJACENT("adjacent");

            @JsonValue
            private final String type;
        }

        @ToString
        @EqualsAndHashCode
        @Builder
        @Getter
        public static class Requirements {
            private final Map<String, String> pis;

            private final AisRequirements ais;

            @Builder
            @ToString
            @EqualsAndHashCode
            @Getter
            public static class AisRequirements {

                private final List<Scope> scopes;

                @RequiredArgsConstructor
                @Getter
                public enum Scope {
                    ACCOUNTS("accounts"),
                    BALANCE("balance"),
                    ;

                    @JsonValue
                    private final String value;
                }
            }
        }
    }

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    public static class Form {
        List<Input.Type> inputTypes;
    }
}
