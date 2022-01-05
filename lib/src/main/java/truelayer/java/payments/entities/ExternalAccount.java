package truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalAccount implements BaseBeneficiary {
    @JsonProperty("type")
    private final String type = "external_account";

    @JsonProperty("name")
    private String name;

    @JsonProperty("reference")
    private String reference;

    @JsonProperty("scheme_identifier")
    private SchemeIdentifier schemeIdentifier;

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SchemeIdentifier {
        @JsonProperty("type")
        private final String type = "sort_code_account_number";

        @JsonProperty("sort_code")
        private String sortCode;

        @JsonProperty("account_number")
        private String accountNumber;
    }
}
