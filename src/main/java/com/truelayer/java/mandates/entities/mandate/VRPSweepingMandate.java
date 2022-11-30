package com.truelayer.java.mandates.entities.mandate;

import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.SWEEPING;

import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.mandates.entities.beneficiary.Beneficiary;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VRPSweepingMandate extends Mandate {

    private final Type type = SWEEPING;

    private ProviderSelection providerSelection;

    private Beneficiary beneficiary;

    private String reference;
}
