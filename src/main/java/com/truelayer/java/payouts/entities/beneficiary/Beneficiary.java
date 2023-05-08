package com.truelayer.java.payouts.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * A Payouts specific DTO for beneficiaries used in create payouts requests only.
 * This is deliberately different from the more generic {@link com.truelayer.java.entities.beneficiary.Beneficiary Beneficiary} class.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ExternalAccount.class)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExternalAccount.class, name = "external_account"),
        @JsonSubTypes.Type(value = PaymentSource.class, name = "payment_source"),
        @JsonSubTypes.Type(value = BusinessAccount.class, name = "business_account")
})
public abstract class Beneficiary {
    @JsonIgnore
    public abstract Type getType();

    @JsonIgnore
    public boolean isExternalAccount() {
        return this instanceof ExternalAccount;
    }

    @JsonIgnore
    public boolean isPaymentSource() {
        return this instanceof PaymentSource;
    }

    @JsonIgnore
    public boolean isBusinessAccount() {
        return this instanceof BusinessAccount;
    }

    @JsonIgnore
    public ExternalAccount asExternalAccount(){
        if(!isExternalAccount())
            throw new TrueLayerException(buildErrorMessage());
        return (ExternalAccount) this;
    }

    @JsonIgnore
    public PaymentSource asPaymentSource(){
        if(!isPaymentSource())
            throw new TrueLayerException(buildErrorMessage());
        return (PaymentSource) this;
    }

    public static ExternalAccount.ExternalAccountBuilder externalAccount() {
        return new ExternalAccount.ExternalAccountBuilder();
    }

    public static PaymentSource.PaymentSourceBuilder paymentSource() {
        return new PaymentSource.PaymentSourceBuilder();
    }

    public static BusinessAccount.BusinessAccountBuilder businessAccount() {
        return new BusinessAccount.BusinessAccountBuilder();
    }

    @JsonIgnore
    public BusinessAccount asBusinessAccount(){
        if(!isBusinessAccount())
            throw new TrueLayerException(buildErrorMessage());
        return (BusinessAccount) this;
    }

    private String buildErrorMessage() {
        return String.format("Beneficiary is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        EXTERNAL_ACCOUNT("external_account"),
        PAYMENT_SOURCE("payment_source"),
        BUSINESS_ACCOUNT("business_account");
        @JsonValue
        private final String type;
    }
}
