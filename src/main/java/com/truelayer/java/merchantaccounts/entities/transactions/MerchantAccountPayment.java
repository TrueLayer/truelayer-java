package com.truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.truelayer.java.entities.CurrencyCode;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class MerchantAccountPayment extends Transaction {
    Type type = Type.MERCHANT_ACCOUNT_PAYMENT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status = Status.SETTLED;

    ZonedDateTime settledAt;

    PaymentSource paymentSource;

    String paymentId;

    Map<String, String> metadata;

    @JsonGetter
    public Optional<Map<String, String>> getMetadata() {
        return Optional.ofNullable(metadata);
    }
}
