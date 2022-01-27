package truelayer.java.payments.entities.paymentdetail;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Redirect extends AuthorizationFlowAction {
    String type = "redirect";

    String uri;

    Metadata metadata;

    @Value
    public static class Metadata {
        String type = "provider";

        String providerId;

        String displayName;

        String iconUri;

        String logoUri;

        String bgColor;

        String countryCode;
    }
}
