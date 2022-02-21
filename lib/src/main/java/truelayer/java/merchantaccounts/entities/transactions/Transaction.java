package truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.entities.PaymentSource;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = MerchantAccountPayment.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MerchantAccountPayment.class, name = "merchant_account_payment"),
    @JsonSubTypes.Type(value = ExternalPayment.class, name = "external_payment"),
    @JsonSubTypes.Type(value = Payout.class, name = "external_payment"),
})
@Getter
public abstract class Transaction {

    private String id;

    private CurrencyCode currency;

    private int amountInMinor;

    private Status status;

    private String settledAt;

    private PaymentSource paymentSource;

    String paymentId;

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        MERCHANT_ACCOUNT_PAYMENT("merchant_account_payment"),
        EXTERNAL_PAYMENT("external_payment"),
        PAYOUT("payout");

        @JsonValue
        private final String transactionType;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        SETTLED("settled"),
        PENDING("pending");

        @JsonValue
        private final String status;
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
