package com.truelayer.java.entities;

import java.net.URI;
import java.util.List;
import lombok.Value;

@Value
public class Provider {
    String id;

    String displayName;

    URI iconUri;

    URI logoUri;

    String bgColor;

    String countryCode;

    ProviderAvailability availability;

    List<String> searchAliases;
}
