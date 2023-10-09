package com.truelayer.java.merchantaccounts.entities.transactions;

import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.Value;

/**
 * A transaction item specific DTO for external payments
 * representing account identifiers of either IBAN or SCAN type.
 * This is deliberately different from the more generic
 * {@link com.truelayer.java.entities.Remitter} class.
 */
@Value
public class Remitter {
    private List<AccountIdentifier> accountIdentifiers;

    private String accountHolderName;

    private String reference;
}
