package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import truelayer.java.payments.entities.beneficiary.ExternalAccount;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        defaultImpl = SortCodeAccountNumberAccountIdentifier.class)
@JsonSubTypes({
    // this is instructing Jackson to deserialize into a MerchantAccount or ExternalAccount type
    // based on the value coming in the beneficiary JSON object with key "type"
    @JsonSubTypes.Type(value = MerchantAccount.class, name = "merchant_account"),
    @JsonSubTypes.Type(value = ExternalAccount.class, name = "external_account")
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
        IBAN("iban");

        @JsonValue
        private final String type;
    }
}
