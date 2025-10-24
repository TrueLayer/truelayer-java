package com.truelayer.java.payouts.entities.beneficiary.providerselection;

import com.truelayer.java.entities.ProviderFilter;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserSelectedProviderSelection extends ProviderSelection {
    private final Type type = Type.USER_SELECTED;

    private ProviderFilter filter;
}
