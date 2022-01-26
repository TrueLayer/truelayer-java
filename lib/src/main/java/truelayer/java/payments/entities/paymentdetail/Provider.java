package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Provider {
    private String providerId;

    private String displayName;

    private String iconUri;

    private String logoUri;

    private String bgColor;

    private String countryCode;
}
