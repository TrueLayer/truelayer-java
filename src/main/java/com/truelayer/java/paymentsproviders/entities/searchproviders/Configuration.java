package com.truelayer.java.paymentsproviders.entities.searchproviders;

import lombok.*;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Configuration {
    private ProviderSelection providerSelection;

    private Redirect redirect;

    private Form form;

    private Consent consent;
}
