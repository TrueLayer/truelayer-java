package truelayer.java.payments.entities.paymentdetail;

import lombok.Value;

@Value
public class Provider {
    private String providerId;

    private String displayName;

    private String iconUri;

    private String logoUri;

    private String bgColor;

    private String countryCode;
}
