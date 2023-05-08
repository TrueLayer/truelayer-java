package com.truelayer.java.payments.entities.providerselection;

import com.truelayer.java.entities.Remitter;
import com.truelayer.java.entities.SchemeId;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PreselectedProviderSelection extends ProviderSelection {
    private final Type type = Type.PRESELECTED;

    private Remitter remitter;

    private String providerId;

    /**
     * The id of the scheme to make the payment over. In responses, it will be populated when the provider selection has been submitted.
     */
    private SchemeId schemeId;

    private String dataAccessToken;
}
