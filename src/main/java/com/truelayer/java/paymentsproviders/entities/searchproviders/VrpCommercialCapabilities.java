package com.truelayer.java.paymentsproviders.entities.searchproviders;

import com.truelayer.java.entities.UseCase;
import java.util.List;
import lombok.Value;

@Value
public class VrpCommercialCapabilities {
    List<UseCase> useCases;
}
