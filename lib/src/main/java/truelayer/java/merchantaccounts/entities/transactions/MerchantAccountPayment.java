package truelayer.java.merchantaccounts.entities.transactions;

import java.util.List;
import lombok.*;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.accountidentifier.AccountIdentifier;

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

    @Value
    public static class PaymentSource {
        String id;

        List<AccountIdentifier> accountIdentifiers;

        String accountHolderName;

        String userId;
    }
}
