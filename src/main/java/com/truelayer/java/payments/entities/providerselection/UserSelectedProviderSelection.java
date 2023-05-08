package com.truelayer.java.payments.entities.providerselection;

import com.truelayer.java.entities.ProviderFilter;
import com.truelayer.java.entities.SchemeId;
import com.truelayer.java.payments.entities.schemeselection.SchemeSelection;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSelectedProviderSelection extends ProviderSelection {
    private final Type type = Type.USER_SELECTED;

    private ProviderFilter filter;

    /**
     * Provider id field only available in responses, once the provider has been submitted
     */
    private String providerId;

    private SchemeId schemeId;

    private SchemeSelection schemeSelection;
}
