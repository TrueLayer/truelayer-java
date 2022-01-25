package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Redirect extends BaseAuthorizationFlowAction {
    String type = "redirect";

    String uri;

    Metadata metadata;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
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
