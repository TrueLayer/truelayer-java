package com.truelayer.java.entities.accountidentifier;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class IbanAccountIdentifier extends AccountIdentifier {
    private final Type type = Type.IBAN;

    private String iban;
}
