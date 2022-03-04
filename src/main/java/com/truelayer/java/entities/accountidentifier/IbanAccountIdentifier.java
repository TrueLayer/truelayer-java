package com.truelayer.java.entities.accountidentifier;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IbanAccountIdentifier extends AccountIdentifier {
    private final Type type = Type.IBAN;

    private String iban;
}
