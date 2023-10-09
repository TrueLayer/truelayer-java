package com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SortCodeAccountNumberAccountIdentifier extends AccountIdentifier {
    private final Type type = Type.SORT_CODE_ACCOUNT_NUMBER;

    private String sortCode;

    private String accountNumber;
}
