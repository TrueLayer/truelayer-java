package truelayer.java.payments.entities.beneficiary;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalAccount extends BaseBeneficiary {
    private final String type = "external_account";

    private String name;

    private String reference;

    private SchemeIdentifier schemeIdentifier;

    @Builder
    @Getter
    @ToString
    @EqualsAndHashCode
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SchemeIdentifier {
        private final String type = "sort_code_account_number";

        private String sortCode;

        private String accountNumber;
    }
}
