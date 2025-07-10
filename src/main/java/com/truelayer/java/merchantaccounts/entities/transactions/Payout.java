package com.truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.beneficiary.Beneficiary;
import com.truelayer.java.payouts.entities.SchemeId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * DTO class that represents both <code>pending</code> and <code>executed</code> payouts.
 * We're not defining explicit variants in this case based on the <code>status</code> field because
 * it seems that the library used for deserialization is not capable of handling more than one deserialization hints,
 * which would be required in this case to first select a DTO based on the <code>type</code> and then on the
 * <code>status</code> fields.
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class Payout extends Transaction {

    Transaction.Type type = Transaction.Type.PAYOUT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    /**
     * Represents the status of the payout.
     * Either <code>pending</code> or <code>executed</code>.
     */
    Transaction.Status status;

    ZonedDateTime createdAt;

    ZonedDateTime executedAt;

    Beneficiary beneficiary;

    Payout.ContextCode contextCode;

    String payoutId;

    SchemeId schemeId;

    Map<String, String> metadata;

    @JsonGetter
    public Optional<Map<String, String>> getMetadata() {
        return Optional.ofNullable(metadata);
    }

    @JsonGetter
    public Optional<ZonedDateTime> getExecutedAt() {
        return Optional.ofNullable(executedAt);
    }

    @JsonGetter
    public Optional<SchemeId> getSchemeId() {
        return Optional.ofNullable(schemeId);
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
