package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.payments.entities.paymentmethod.PaymentSchemeIdentifier.Type.SORT_CODE_ACCOUNT_NUMBER;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SortCodeAccountNumberSchemeIdentifier extends PaymentSchemeIdentifier {

    private final Type type = SORT_CODE_ACCOUNT_NUMBER;

    private final String sortCode;

    private final String accountNumber;
}
