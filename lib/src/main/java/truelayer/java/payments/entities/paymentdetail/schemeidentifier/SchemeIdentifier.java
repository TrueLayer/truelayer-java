package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = SortCodeAccountNumber.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SortCodeAccountNumber.class, name = "sort_code_account_number"),
    @JsonSubTypes.Type(value = Iban.class, name = "iban"),
    @JsonSubTypes.Type(value = Bban.class, name = "bban"),
    @JsonSubTypes.Type(value = Nrb.class, name = "nrb")
})
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class SchemeIdentifier {
    protected Type type;

    public enum Type {
        NRB("nrb"),
        BBAN("bban"),
        IBAN("iban"),
        SORT_CODE_ACCOUNT_NUMBER("sort_code_account_number");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        @JsonValue
        public String getType() {
            return type;
        }
    }
}
