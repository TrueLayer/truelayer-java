package com.truelayer.java.entities.beneficiary;

import static com.truelayer.java.entities.beneficiary.Beneficiary.Type.EXTERNAL_ACCOUNT;

import com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExternalAccount extends Beneficiary {
    Type type = EXTERNAL_ACCOUNT;

    String reference;

    String accountHolderName;

    List<AccountIdentifier> accountIdentifiers;
}
