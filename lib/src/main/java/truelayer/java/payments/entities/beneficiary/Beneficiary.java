package truelayer.java.payments.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.TrueLayerException;

@JsonTypeInfo(
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
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

    protected Type type;

    @JsonIgnore
    public boolean isMerchantAccount() {
        return this instanceof MerchantAccount;
    }

    @JsonIgnore
    public boolean isExternalAccount() {
        return this instanceof ExternalAccount;
    }

    public MerchantAccount asMerchantAccount() {
        if (!isMerchantAccount()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (MerchantAccount) this;
    }

    public ExternalAccount asExternalAccount() {
        if (!isExternalAccount()) {
            throw new TrueLayerException(buildErrorMessage());
        }
        return (ExternalAccount) this;
    }

    private String buildErrorMessage() {
        return String.format(
                "beneficiary is of type %1$s. Consider using as%1$s() instead.",
                this.getClass().getSimpleName());
    }

    public enum Type{
        EXTERNAL_ACCOUNT("external_account"),
        MERCHANT_ACCOUNT("merchant_account");

        private String type;

        Type(String type) {
            this.type = type;
        }

        @JsonValue
        public String getType() {
            return type;
        }
    }
}
