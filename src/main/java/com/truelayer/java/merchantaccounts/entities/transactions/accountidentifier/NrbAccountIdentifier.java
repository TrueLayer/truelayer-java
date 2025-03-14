package com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NrbAccountIdentifier extends AccountIdentifier {
    private final Type type = Type.NRB;

    private String nrb;
}
