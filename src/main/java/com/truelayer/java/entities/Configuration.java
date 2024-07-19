package com.truelayer.java.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.payments.entities.paymentdetail.RedirectStatus;
import lombok.Value;

import java.util.Optional;

@Value
public class Configuration {
    ProviderSelection providerSelection;

    RedirectStatus redirect;

    Retry retry;

    @Value
    public static class ProviderSelection {
        ConfigurationStatus status;
    }

    @JsonGetter
    public Optional<Retry> getRetry() {
        return Optional.ofNullable(retry);
    }
}
