package truelayer.java.merchantaccounts.entities.transactions;

import lombok.*;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.merchantaccounts.entities.PaymentSource;

@Value
@EqualsAndHashCode(callSuper = false)
public class MerchantAccountPayment extends Transaction {
    Type type = Type.MERCHANT_ACCOUNT_PAYMENT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    String settledAt;

    PaymentSource paymentSource;

    String paymentId;
}
