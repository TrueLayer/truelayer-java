package com.truelayer.java.merchantaccounts.entities.transactions;

import com.truelayer.java.entities.CurrencyCode;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class MerchantAccountPayment extends Transaction {
    Type type = Type.MERCHANT_ACCOUNT_PAYMENT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    ZonedDateTime settledAt;

    PaymentSource paymentSource;

    String paymentId;
}
