package com.truelayer.java.payouts.entities.beneficiary.providerselection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class PreselectedProviderSelection extends ProviderSelection {
    private final Type type = Type.PRESELECTED;

    private String providerId;
}
