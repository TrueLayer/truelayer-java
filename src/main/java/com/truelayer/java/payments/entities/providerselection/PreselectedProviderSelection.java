package com.truelayer.java.payments.entities.providerselection;

import com.truelayer.java.entities.Remitter;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import com.truelayer.java.payments.entities.SchemeId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreselectedProviderSelection extends ProviderSelection {
    private final Type type = Type.PRESELECTED;

    private Remitter remitter;

    protected String providerId;

    /**
     * The id of the scheme to make the payment over. Will be populated when the provider selection has been submitted.
     * Only used in single payments.
     */
    protected SchemeId schemeId;
}
