package truelayer.java.merchantaccounts.entities.transactions;

import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.beneficiary.Beneficiary;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPayout {

    Transaction.Type type = Transaction.Type.PAYOUT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status = Transaction.Status.SETTLED;

    String createdAt;

    String settledAt;

    Beneficiary beneficiary;

    Payout.ContextCode contextCode;

    String payoutId;
}
