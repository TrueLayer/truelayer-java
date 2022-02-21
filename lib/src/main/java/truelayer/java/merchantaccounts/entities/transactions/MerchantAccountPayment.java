package truelayer.java.merchantaccounts.entities.transactions;

import lombok.EqualsAndHashCode;
import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.PaymentSource;
import truelayer.java.merchantaccounts.entities.transactions.Transaction.Type;
import truelayer.java.payments.entities.paymentdetail.*;

@Value
@EqualsAndHashCode(callSuper = false)
public class MerchantAccountPayment {
    Type type = Type.MERCHANT_ACCOUNT_PAYMENT;

    String id;

    CurrencyCode currency;

    int amountInMinor;

    Transaction.Status status;

    String settledAt;

    PaymentSource paymentSource;

    String paymentId;
}
