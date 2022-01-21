package truelayer.java.payments.entities.paymentmethod;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SortCodeAccountNumberSchemeIdentifier extends BasePaymentSchemeIdentifier {

    private final String type = "sort_code_account_number";

    private final String sortCode;

    private final String accountNumber;
}
