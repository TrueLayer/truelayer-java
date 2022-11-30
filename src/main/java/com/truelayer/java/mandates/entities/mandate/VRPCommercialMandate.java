package com.truelayer.java.mandates.entities.mandate;

import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.COMMERCIAL;

import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.mandates.entities.beneficiary.Beneficiary;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VRPCommercialMandate extends Mandate {

    private final Type type = COMMERCIAL;

    private ProviderSelection providerSelection;

    private Beneficiary beneficiary;

    private String reference;
}
