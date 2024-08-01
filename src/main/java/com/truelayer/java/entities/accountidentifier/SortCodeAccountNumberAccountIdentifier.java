package com.truelayer.java.entities.accountidentifier;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class SortCodeAccountNumberAccountIdentifier extends AccountIdentifier {
    private final Type type = Type.SORT_CODE_ACCOUNT_NUMBER;

    private String sortCode;

    private String accountNumber;
}
