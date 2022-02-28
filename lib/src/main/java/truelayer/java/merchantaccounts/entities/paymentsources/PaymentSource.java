package truelayer.java.merchantaccounts.entities.paymentsources;

import lombok.Value;
import truelayer.java.entities.accountidentifier.AccountIdentifier;

import java.util.List;

@Value
public class PaymentSource {
    String id;

    List<AccountIdentifier> accountIdentifiers;

    String accountHolderName;
}
