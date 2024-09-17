package com.truelayer.java.payments.entities.schemeselection.preselected;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class InstantOnlySchemeSelection extends SchemeSelection {
    private final Type type = Type.INSTANT_ONLY;

    @Accessors(fluent = true)
    @JsonProperty // Jackson's @JsonProperty is required for serialization to work as expected
    private boolean allowRemitterFee;
}
