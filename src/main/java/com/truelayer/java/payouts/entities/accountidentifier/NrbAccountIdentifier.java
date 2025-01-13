package com.truelayer.java.payouts.entities.accountidentifier;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class NrbAccountIdentifier extends AccountIdentifier {
    private final Type type = Type.NRB;

    private String nrb;
}
