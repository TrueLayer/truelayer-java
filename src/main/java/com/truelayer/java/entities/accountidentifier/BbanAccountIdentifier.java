package com.truelayer.java.entities.accountidentifier;

import static com.truelayer.java.entities.accountidentifier.AccountIdentifier.Type.BBAN;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BbanAccountIdentifier extends AccountIdentifier {
    private final Type type = BBAN;

    private String bban;
}
