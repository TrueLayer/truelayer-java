package truelayer.java.merchantaccounts.entities.transactions;

import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.beneficiary.Beneficiary;

@Value
@EqualsAndHashCode(callSuper = false)
public class Payout {

    Transaction.Type type = Transaction.Type.PAYOUT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    String createdAt;

    String settledAt;

    Beneficiary beneficiary;

    Transaction.ContextCode contextCode;

    String payoutId;

    public Optional<String> getSettledAt() {
        return Optional.ofNullable(settledAt);
    }
}
