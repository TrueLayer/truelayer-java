package com.truelayer.java.entities.providerselection;

import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.payments.entities.SchemeId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSelectedProviderSelection extends ProviderSelection {
    private final Type type = Type.USER_SELECTED;

    private ProviderFilter filter;

    protected String providerId;

    protected SchemeId schemeId;
}
