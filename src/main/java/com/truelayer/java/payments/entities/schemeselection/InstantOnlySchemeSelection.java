package com.truelayer.java.payments.entities.schemeselection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class InstantOnlySchemeSelection extends SchemeSelection {
    private final Type type = Type.INSTANT_ONLY;

    @Accessors(fluent = true)
    private boolean allowRemitterFee;
}
