package com.truelayer.java.payments.entities;

import java.net.URI;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class HostedPageParameters {
    private URI returnUri;

    private CountryCode countryCode;

    /**
     * A valid ISO 639-1 language code
     */
    private String languageCode;

    /**
     * The maximum time to wait for a result from the hosted page after the user has completed the authorization flow, in seconds.
     */
    private int maxWaitForResults;
}
