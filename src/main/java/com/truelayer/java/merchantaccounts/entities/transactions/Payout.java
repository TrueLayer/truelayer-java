package com.truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.payments.entities.beneficiary.Beneficiary;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Payout extends Transaction {

    Transaction.Type type = Transaction.Type.PAYOUT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    ZonedDateTime createdAt;

    ZonedDateTime settledAt;

    Beneficiary beneficiary;

    Payout.ContextCode contextCode;

    String payoutId;

    public Optional<ZonedDateTime> getSettledAt() {
        return Optional.ofNullable(settledAt);
    }

    @RequiredArgsConstructor
    @Getter
    public enum ContextCode {
        WITHDRAWAL("withdrawal"),
        SERVICE_PAYMENT("service_payment"),
        INTERNAL("internal");

        @JsonValue
        private final String status;
    }
}
