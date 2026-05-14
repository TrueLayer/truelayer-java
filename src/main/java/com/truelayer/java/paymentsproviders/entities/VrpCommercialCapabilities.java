package com.truelayer.java.paymentsproviders.entities;

import com.truelayer.java.entities.ProviderAvailability;
import com.truelayer.java.payments.entities.ReleaseChannel;
import com.truelayer.java.entities.UseCase;
import lombok.Value;

import java.util.List;

@Value
public class VrpCommercialCapabilities {
    ReleaseChannel releaseChannel;

    ProviderAvailability availability;

    List<UseCase> supportedUseCases;
}
