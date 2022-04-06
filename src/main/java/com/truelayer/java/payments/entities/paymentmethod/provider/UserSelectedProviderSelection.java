package com.truelayer.java.payments.entities.paymentmethod.provider;

import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.payments.entities.SchemeId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSelectedProviderSelection extends ProviderSelection {
    private final Type type = Type.USER_SELECTED;

    protected String providerId;

    protected SchemeId schemeId;

    private ProviderFilter filter;
}
