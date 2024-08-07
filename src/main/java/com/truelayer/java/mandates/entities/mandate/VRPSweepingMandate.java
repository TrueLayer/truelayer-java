package com.truelayer.java.mandates.entities.mandate;

import static com.truelayer.java.mandates.entities.mandate.Mandate.Type.SWEEPING;

import com.truelayer.java.mandates.entities.beneficiary.Beneficiary;
import com.truelayer.java.payments.entities.providerselection.ProviderSelection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class VRPSweepingMandate extends Mandate {

    private final Type type = SWEEPING;

    private ProviderSelection providerSelection;

    private Beneficiary beneficiary;

    private String reference;
}
