package truelayer.java.merchantaccounts.entities;

import java.util.List;
import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.accountidentifier.AccountIdentifier;

@Value
public class MerchantAccount {
    String id;

    CurrencyCode currency;

    List<AccountIdentifier> accountIdentifiers;

    int availableBalanceInMinor;

    int currentBalanceInMinor;

    String accountHolderName;
}
