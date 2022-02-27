package com.truelayer.java.payments.entities.paymentdetail;

import java.net.URI;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ProviderMetadata extends Metadata {
    Type type = Type.PROVIDER;

    String providerId;

    String displayName;

    URI iconUri;

    URI logoUri;

    String bgColor;

    String countryCode;
}
