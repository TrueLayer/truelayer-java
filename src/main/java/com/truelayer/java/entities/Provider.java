package com.truelayer.java.entities;

import java.net.URI;
import lombok.Value;

@Value
public class Provider {
    String id;

    String displayName;

    URI iconUri;

    URI logoUri;

    String bgColor;

    String countryCode;
}
