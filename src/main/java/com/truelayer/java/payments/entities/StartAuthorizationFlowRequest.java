package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.payments.entities.paymentdetail.forminput.Input;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lombok.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class StartAuthorizationFlowRequest {

    private final ProviderSelection providerSelection;

    private final Map<String, String> schemeSelection;

    private final Redirect redirect;

    private final Consent consent;

    private final Form form;

    private final Map<String, String> userAccountSelection;

    @Builder
    @ToString
    @EqualsAndHashCode
    public static class ProviderSelection {
        private final Icon icon;

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
        public static class Requirements {
            private final Map<String, String> pis;

            @Builder
            @ToString
            @EqualsAndHashCode
            public static class AisRequirements {

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
