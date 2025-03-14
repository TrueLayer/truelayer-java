package com.truelayer.java.entities.beneficiary;

import static com.truelayer.java.entities.beneficiary.Beneficiary.Type.BUSINESS_ACCOUNT;

import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class BusinessAccount extends Beneficiary {
    Type type = BUSINESS_ACCOUNT;

    String reference;

    String accountHolderName;

    List<AccountIdentifier> accountIdentifiers;
}
