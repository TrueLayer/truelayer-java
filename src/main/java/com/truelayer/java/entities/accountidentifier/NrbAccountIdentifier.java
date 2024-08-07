package com.truelayer.java.entities.accountidentifier;

import static com.truelayer.java.entities.accountidentifier.AccountIdentifier.Type.NRB;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class NrbAccountIdentifier extends AccountIdentifier {
    private final Type type = NRB;

    private String nrb;
}
