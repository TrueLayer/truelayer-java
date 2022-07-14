package com.truelayer.java.mandates.entities.providerselection;

import com.truelayer.java.entities.Remitter;
import com.truelayer.java.entities.providerselection.ProviderSelection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreselectedProviderSelection extends ProviderSelection {
    private final Type type = Type.PRESELECTED;

    protected String providerId;

    private Remitter remitter;
}
