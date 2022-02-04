package truelayer.java.payments.entities.paymentdetail;

import java.net.URI;
import lombok.Value;

@Value
public class Provider {
    String providerId;

    String displayName;

    URI iconUri;

    URI logoUri;

    String bgColor;

    String countryCode;
}
