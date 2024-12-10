package com.truelayer.java.paymentsproviders.entities.searchproviders;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.payments.entities.CountryCode;
import com.truelayer.java.payments.entities.CustomerSegment;
import com.truelayer.java.payments.entities.ReleaseChannel;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SearchPaymentProvidersRequest {
    private List<CountryCode> countries;

    private List<CurrencyCode> currencies;

    private ReleaseChannel releaseChannel;

    private List<CustomerSegment> customerSegments;

    private Capabilities capabilities;

    private AuthorizationFlow authorizationFlow;
}
