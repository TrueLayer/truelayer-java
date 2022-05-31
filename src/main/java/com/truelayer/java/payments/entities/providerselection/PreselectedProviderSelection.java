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

    protected String providerId;

    protected SchemeId schemeId;

    private Remitter remitter;
}
