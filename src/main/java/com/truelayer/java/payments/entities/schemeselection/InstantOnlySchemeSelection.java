package com.truelayer.java.payments.entities.schemeselection;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder
public class InstantOnlySchemeSelection extends SchemeSelection {
    private final Type type = Type.INSTANT_ONLY;

    @Accessors(fluent = true)
    private boolean allowRemitterFee;
}
