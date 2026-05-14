package com.truelayer.java.paymentsproviders.entities;

import com.truelayer.java.entities.ProviderAvailability;
import com.truelayer.java.entities.UseCase;
import com.truelayer.java.payments.entities.ReleaseChannel;
import java.util.List;
import lombok.Value;

@Value
public class VrpCommercialCapabilities {
    ReleaseChannel releaseChannel;

    ProviderAvailability availability;

    List<UseCase> supportedUseCases;
}
