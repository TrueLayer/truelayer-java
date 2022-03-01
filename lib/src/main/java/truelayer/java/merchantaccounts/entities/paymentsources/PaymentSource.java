package truelayer.java.merchantaccounts.entities.paymentsources;

import java.util.List;
import lombok.Value;
import truelayer.java.entities.accountidentifier.AccountIdentifier;

@Value
public class PaymentSource {
    String id;

    List<AccountIdentifier> accountIdentifiers;

    String accountHolderName;
}
