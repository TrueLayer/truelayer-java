package com.truelayer.java.payouts.entities.schemeselection;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class PreselectedSchemeSelection extends SchemeSelection {

    private final Type type = Type.PRESELECTED;

    private String schemeId;
}
