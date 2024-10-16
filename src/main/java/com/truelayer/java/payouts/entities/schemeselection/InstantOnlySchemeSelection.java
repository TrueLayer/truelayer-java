package com.truelayer.java.payouts.entities.schemeselection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class InstantOnlySchemeSelection extends SchemeSelection {
    private final Type type = Type.INSTANT_ONLY;
}
