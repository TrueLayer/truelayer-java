package com.truelayer.java.merchantaccounts.entities.transactions.beneficiary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * A transaction item specific DTO for payouts beneficiaries. Unlike the shared
 * {@link com.truelayer.java.payments.entities.beneficiary.Beneficiary Beneficiary} type, this abstract class is
 * subclassed by concrete variants that represent one of <code>external_account</code>, <code>business_account</code> or
 * <code>payment_source</code>.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ExternalAccount.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ExternalAccount.class, name = "external_account"),
    @JsonSubTypes.Type(value = BusinessAccount.class, name = "business_account"),
    @JsonSubTypes.Type(value = PaymentSource.class, name = "payment_source")
})
@ToString
@EqualsAndHashCode
@Getter
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
    public BusinessAccount asBusinessAccount() {
        if (!isBusinessAccount()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (BusinessAccount) this;
    }

    @JsonIgnore
    public ExternalAccount asExternalAccount() {
        if (!isExternalAccount()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ExternalAccount) this;
    }

    @JsonIgnore
    public PaymentSource asPaymentSource() {
        if (!isPaymentSource()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (PaymentSource) this;
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
