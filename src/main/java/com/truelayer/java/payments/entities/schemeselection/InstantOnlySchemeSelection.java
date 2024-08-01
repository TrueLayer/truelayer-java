package com.truelayer.java.payments.entities.schemeselection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class InstantOnlySchemeSelection extends SchemeSelection {
    private final Type type = Type.INSTANT_ONLY;

    private boolean allowRemitterFee;
}
