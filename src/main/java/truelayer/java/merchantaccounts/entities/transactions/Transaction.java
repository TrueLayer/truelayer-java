package truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import truelayer.java.TrueLayerException;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = MerchantAccountPayment.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MerchantAccountPayment.class, name = "merchant_account_payment"),
    @JsonSubTypes.Type(value = ExternalPayment.class, name = "external_payment"),
    @JsonSubTypes.Type(value = Payout.class, name = "payout"),
})
@Getter
public abstract class Transaction {

    public abstract Type getType();

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        MERCHANT_ACCOUNT_PAYMENT("merchant_account_payment"),
        EXTERNAL_PAYMENT("external_payment"),
        PAYOUT("payout");

        @JsonValue
        private final String type;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {
        SETTLED("settled"),
        PENDING("pending");

        @JsonValue
        private final String status;
    }

    public MerchantAccountPayment asMerchantAccountPayment() {
        if (!(this instanceof MerchantAccountPayment)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (MerchantAccountPayment) this;
    }

    public ExternalPayment asExternalPayment() {
        if (!(this instanceof ExternalPayment)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ExternalPayment) this;
    }

    public Payout asPayout() {
        if (!(this instanceof Payout)) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Payout) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "payment is of type %1$s. Consider using as%1$s() instead.",
                this.getClass().getSimpleName());
    }
}
