package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.accountidentifier.AccountIdentifier;
import java.util.List;
import lombok.Value;

@Value
public class MerchantAccount {
    String id;

    CurrencyCode currency;

    List<AccountIdentifier> accountIdentifiers;

    int availableBalanceInMinor;

    int currentBalanceInMinor;

    String accountHolderName;
}
