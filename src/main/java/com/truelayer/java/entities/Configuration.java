package com.truelayer.java.entities;

import com.truelayer.java.payments.entities.paymentdetail.RedirectStatus;
import lombok.Value;

@Value
public class Configuration {
    ProviderSelection providerSelection;

    RedirectStatus redirect;

    @Value
    public static class ProviderSelection {
        ConfigurationStatus status;
    }
}
