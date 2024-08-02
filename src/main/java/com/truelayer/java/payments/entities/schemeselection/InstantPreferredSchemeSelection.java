package com.truelayer.java.payments.entities.schemeselection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
public class InstantPreferredSchemeSelection extends SchemeSelection {
    private final Type type = Type.INSTANT_PREFERRED;

    // Apparently there's a problem with fluent setters and serialization...
    // https://stackoverflow.com/questions/72570822/accessorsfluent-true-does-not-work-with-jakson

    // @Accessors(fluent = true)
    private boolean allowRemitterFee;
}
