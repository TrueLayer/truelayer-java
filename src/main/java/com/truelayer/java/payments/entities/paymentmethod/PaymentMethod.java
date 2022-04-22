package com.truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.payments.entities.paymentdetail.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = BankTransfer.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = BankTransfer.class, name = "bank_transfer"),
    @JsonSubTypes.Type(value = Mandate.class, name = "mandate")
})
@Getter
@ToString
@EqualsAndHashCode
public abstract class PaymentMethod {

    protected Type type;

    @JsonIgnore
    public BankTransfer asBankTransfer() {
        if (!isBankTransfer()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (BankTransfer) this;
    }

    @JsonIgnore
    public Mandate asMandate() {
        if (!isMandate()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (Mandate) this;
    }

    public static BankTransfer.BankTransferBuilder bankTransfer() {
        return new BankTransfer.BankTransferBuilder();
    }

    public static Mandate.MandateBuilder mandate() {
        return new Mandate.MandateBuilder();
    }

    @JsonIgnore
    public boolean isBankTransfer() {
        return this instanceof BankTransfer;
    }

    @JsonIgnore
    public boolean isMandate() {
        return this instanceof Mandate;
    }

    private String buildErrorMessage() {
        return String.format("Payment method is of type %s.", this.getClass().getSimpleName());
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        BANK_TRANSFER("bank_transfer"),
        MANDATE("mandate");

        @JsonValue
        private final String type;
    }
}
