package com.truelayer.java.merchantaccounts.entities.transactions.accountidentifier;

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
 * A transaction item specific DTO for external payments
 * representing account identifiers of either IBAN or SCAN type only.
 * This is deliberately different from the more generic {@link com.truelayer.java.entities.Remitter Remitter} class.
 */
@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = SortCodeAccountNumberAccountIdentifier.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SortCodeAccountNumberAccountIdentifier.class, name = "sort_code_account_number"),
    @JsonSubTypes.Type(value = IbanAccountIdentifier.class, name = "iban"),
    @JsonSubTypes.Type(value = NrbAccountIdentifier.class, name = "nrb"),
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class AccountIdentifier {
    public abstract Type getType();

    @JsonIgnore
    public boolean isSortCodeAccountNumberIdentifier() {
        return this instanceof SortCodeAccountNumberAccountIdentifier;
    }

    @JsonIgnore
    public boolean isIbanIdentifier() {
        return this instanceof IbanAccountIdentifier;
    }

    @JsonIgnore
    public SortCodeAccountNumberAccountIdentifier asSortCodeAccountNumber() {
        if (!isSortCodeAccountNumberIdentifier()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SortCodeAccountNumberAccountIdentifier) this;
    }

    @JsonIgnore
    public IbanAccountIdentifier asIban() {
        if (!isIbanIdentifier()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (IbanAccountIdentifier) this;
    }

    private String buildErrorMessage() {
        return String.format("Identifier is of type %s.", this.getClass().getSimpleName());
    }

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        SORT_CODE_ACCOUNT_NUMBER("sort_code_account_number"),
        IBAN("iban"),
        NRB("nrb");

        @JsonValue
        private final String type;
    }
}
