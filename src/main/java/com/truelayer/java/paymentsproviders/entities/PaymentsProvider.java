package com.truelayer.java.paymentsproviders.entities;

import java.net.URI;
import lombok.Value;

@Value
public class PaymentsProvider {
    String id;

    String displayName;

    URI iconUri;

    URI logoUri;

    String bgColor;

    String countryCode;

    Capabilities capabilities;
}
