package truelayer.java.payments.entities.paymentdetail;

import lombok.Value;

@Value
public class Provider {
    String providerId;

    String displayName;

    String iconUri;

    String logoUri;

    String bgColor;

    String countryCode;
}
