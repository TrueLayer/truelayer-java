package com.truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.entities.CurrencyCode;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Refund extends Transaction {
    Type type = Type.REFUND;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    /**
     * Represents the refund status, either <code>pending</code> or <code>executed</code>.
     */
    Transaction.Status status;

    ZonedDateTime createdAt;

    ZonedDateTime executedAt;

    com.truelayer.java.merchantaccounts.entities.transactions.beneficiary.PaymentSource beneficiary;

    Payout.ContextCode contextCode;

    String refundId;

    String paymentId;

    @JsonGetter
    public Optional<ZonedDateTime> getExecutedAt() {
        return Optional.ofNullable(executedAt);
    }
}
