package com.truelayer.java.merchantaccounts.entities.transactions.beneficiary;

import static com.truelayer.java.merchantaccounts.entities.transactions.beneficiary.Beneficiary.Type.BUSINESS_ACCOUNT;

import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class BusinessAccount extends Beneficiary {
    private final Type type = BUSINESS_ACCOUNT;

    private String reference;

    private String accountHolderName;

    private List<AccountIdentifier> accountIdentifiers;
}
