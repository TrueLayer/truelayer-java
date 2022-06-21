package com.truelayer.java.merchantaccounts.entities.transactions;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.Remitter;
import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExternalPayment extends Transaction {

    Transaction.Type type = Transaction.Type.EXTERNAL_PAYMENT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    ZonedDateTime settledAt;

    Remitter remitter;
}
