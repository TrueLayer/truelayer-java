package truelayer.java.merchantaccounts.entities.transactions;

import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.Remitter;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExternalPayment {

    Transaction.Type type = Transaction.Type.EXTERNAL_PAYMENT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    String settledAt;

    Remitter remitter;
}
