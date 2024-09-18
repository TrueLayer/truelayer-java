package com.truelayer.java.payments.entities.schemeselection.userselected;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserSelectedSchemeSelection extends SchemeSelection {
    private final Type type = Type.USER_SELECTED;
}
