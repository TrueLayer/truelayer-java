package truelayer.java.payments.entities.paymentdetail;

import java.util.List;
import lombok.Value;
import truelayer.java.entities.AccountIdentifier;

@Value
public class PaymentSource {
    String id;

    List<AccountIdentifier> accountIdentifiers;

    String accountHolderName;
}
