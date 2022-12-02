package com.truelayer.java.mandates.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.truelayer.java.TrueLayerException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = MerchantAccount.class)
@JsonSubTypes({
    // this is instructing Jackson to deserialize into a MerchantAccount or ExternalAccount type
    // based on the value coming in the beneficiary JSON object with key "type"
    @JsonSubTypes.Type(value = MerchantAccount.class, name = "merchant_account"),
    @JsonSubTypes.Type(value = ExternalAccount.class, name = "external_account")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class Beneficiary {

    @JsonIgnore
    public abstract Type getType();

    @JsonIgnore
    public boolean isMerchantAccount() {
        return this instanceof MerchantAccount;
    }

    @JsonIgnore
    public boolean isExternalAccount() {
        return this instanceof ExternalAccount;
    }

    @JsonIgnore
    public MerchantAccount asMerchantAccount() {
        if (!isMerchantAccount()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (MerchantAccount) this;
    }

    @JsonIgnore
    public ExternalAccount asExternalAccount() {
        if (!isExternalAccount()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ExternalAccount) this;
    }

    private String buildErrorMessage() {
        return String.format("Beneficiary is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        EXTERNAL_ACCOUNT("external_account"),
        MERCHANT_ACCOUNT("merchant_account");

        @JsonValue
        private final String type;
    }

    public static MerchantAccount.MerchantAccountBuilder merchantAccount() {
        return new MerchantAccount.MerchantAccountBuilder();
    }

    public static ExternalAccount.ExternalAccountBuilder externalAccount() {
        return new ExternalAccount.ExternalAccountBuilder();
    }
}
