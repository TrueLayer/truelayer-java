package truelayer.java.payments.entities.paymentmethod;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SortCodeAccountNumberSchemeIdentifier extends PaymentSchemeIdentifier {

    private final String type = "sort_code_account_number";

    private final String sortCode;

    private final String accountNumber;
}
