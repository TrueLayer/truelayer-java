package truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalAccount extends BaseSourceOfFunds {
    private String type = "external_account";

    private List<SchemeIdentifier> schemeIdentifiers;

    private String externalAccountId;

    private String accountHolderName;

    public static class SchemeIdentifier {
        private Type type;

        private String sortCode;

        private String accountNumber;

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
}