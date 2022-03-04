package com.truelayer.java.payments.entities.paymentmethod.provider;

import com.truelayer.java.entities.Remitter;
import com.truelayer.java.payments.entities.SchemeId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PreselectedProviderSelection extends ProviderSelection {
    private final Type type = Type.PRESELECTED;

    private String providerId;

    private SchemeId schemeId;

    private Remitter remitter;
}
