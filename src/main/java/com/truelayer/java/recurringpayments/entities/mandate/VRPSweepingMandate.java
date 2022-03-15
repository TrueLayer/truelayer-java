package com.truelayer.java.recurringpayments.entities.mandate;

import static com.truelayer.java.recurringpayments.entities.mandate.Mandate.Type.SWEEPING;

import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VRPSweepingMandate extends Mandate {

    private final Type type = SWEEPING;

    ProviderFilter providerFilter;

    Beneficiary beneficiary;
}
