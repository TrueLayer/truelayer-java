package com.truelayer.java.entities.accountidentifier;

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
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = SortCodeAccountNumberAccountIdentifier.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SortCodeAccountNumberAccountIdentifier.class, name = "sort_code_account_number"),
    @JsonSubTypes.Type(value = IbanAccountIdentifier.class, name = "iban"),
    @JsonSubTypes.Type(value = BbanAccountIdentifier.class, name = "bban"),
    @JsonSubTypes.Type(value = NrbAccountIdentifier.class, name = "nrb")
})
@ToString
@EqualsAndHashCode
@Getter
public abstract class AccountIdentifier {

    public abstract Type getType();

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        SORT_CODE_ACCOUNT_NUMBER("sort_code_account_number"),
        IBAN("iban"),
        BBAN("bban"),
        NRB("nrb");

        @JsonValue
        private final String type;
    }

    public static SortCodeAccountNumberAccountIdentifier.SortCodeAccountNumberAccountIdentifierBuilder
            sortCodeAccountNumber() {
        return new SortCodeAccountNumberAccountIdentifier.SortCodeAccountNumberAccountIdentifierBuilder();
    }

    public static IbanAccountIdentifier.IbanAccountIdentifierBuilder iban() {
        return new IbanAccountIdentifier.IbanAccountIdentifierBuilder();
    }

    public static BbanAccountIdentifier.BbanAccountIdentifierBuilder bban() {
        return new BbanAccountIdentifier.BbanAccountIdentifierBuilder();
    }

    public static NrbAccountIdentifier.NrbAccountIdentifierBuilder nrb() {
        return new NrbAccountIdentifier.NrbAccountIdentifierBuilder();
    }

    @JsonIgnore
    public boolean isSortCodeAccountNumberIdentifier() {
        return this instanceof SortCodeAccountNumberAccountIdentifier;
    }

    @JsonIgnore
    public boolean isIbanIdentifier() {
        return this instanceof IbanAccountIdentifier;
    }

    @JsonIgnore
    public boolean isBbanIdentifier() {
        return this instanceof BbanAccountIdentifier;
    }

    @JsonIgnore
    public boolean isNrbIdentifier() {
        return this instanceof NrbAccountIdentifier;
    }

    @JsonIgnore
    public SortCodeAccountNumberAccountIdentifier asSortCodeAccountNumber() {
        if (!isSortCodeAccountNumberIdentifier()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (SortCodeAccountNumberAccountIdentifier) this;
    }

    //@JsonIgnore
    public IbanAccountIdentifier asIban() {
        if (!isIbanIdentifier()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (IbanAccountIdentifier) this;
    }

    @JsonIgnore
    public BbanAccountIdentifier asBban() {
        if (!isBbanIdentifier()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (BbanAccountIdentifier) this;
    }

    @JsonIgnore
    public NrbAccountIdentifier asNrb() {
        if (!isNrbIdentifier()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (NrbAccountIdentifier) this;
    }

    private String buildErrorMessage() {
        return String.format("Identifier is of type %s.", this.getClass().getSimpleName());
    }
}
