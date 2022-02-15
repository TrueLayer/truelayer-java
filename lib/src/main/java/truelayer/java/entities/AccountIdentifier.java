package truelayer.java.entities;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import truelayer.java.payments.entities.paymentmethod.SortCodeAccountNumberAccountIdentifier;

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

    protected Type type;

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
}
