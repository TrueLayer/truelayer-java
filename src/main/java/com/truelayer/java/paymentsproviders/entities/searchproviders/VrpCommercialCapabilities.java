package com.truelayer.java.paymentsproviders.entities.searchproviders;

import com.truelayer.java.entities.UseCase;
import lombok.Value;

import java.util.List;

@Value
public class VrpCommercialCapabilities {
    List<UseCase> useCases;
}
