package com.truelayer.java.merchantaccounts.entities.transactions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

    @JsonIgnore
    public boolean isMerchantAccountPayment() {
        return this instanceof MerchantAccountPayment;
    }

    @JsonIgnore
    public boolean isExternalPayment() {
        return this instanceof ExternalPayment;
    }

    @JsonIgnore
    public boolean isPayout() {
        return this instanceof Payout;
    }

    @JsonIgnore
    public MerchantAccountPayment asMerchantAccountPayment() {
        if (!isMerchantAccountPayment()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (MerchantAccountPayment) this;
    }

    @JsonIgnore
    public ExternalPayment asExternalPayment() {
        if (!isExternalPayment()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ExternalPayment) this;
    }

    @JsonIgnore
    public Payout asPayout() {
        if (!isPayout()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Payout) this;
    }

    private String buildErrorMessage() {
        return String.format("Payment is of type %s.", this.getClass().getSimpleName());
    }
}
