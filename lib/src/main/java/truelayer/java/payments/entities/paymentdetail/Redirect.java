package truelayer.java.payments.entities.paymentdetail;

import static truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction.Type.REDIRECT;
import static truelayer.java.payments.entities.paymentdetail.Redirect.Metadata.Type.PROVIDER;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Redirect extends AuthorizationFlowAction {
    Type type = REDIRECT;

    String uri;

    Metadata metadata;

    @Value
    public static class Metadata {
        Metadata.Type type = PROVIDER;

        String providerId;

        String displayName;

        String iconUri;

        String logoUri;

        String bgColor;

        String countryCode;

        @RequiredArgsConstructor
        @Getter
        public enum Type {
            PROVIDER("provider");

            @JsonValue
            private final String type;
        }
    }
}
