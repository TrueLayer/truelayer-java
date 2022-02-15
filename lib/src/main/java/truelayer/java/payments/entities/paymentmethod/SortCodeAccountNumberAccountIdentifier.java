package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.entities.AccountIdentifier.Type.*;

import lombok.Builder;
import lombok.Getter;
import truelayer.java.entities.AccountIdentifier;

@Builder
@Getter
public class SortCodeAccountNumberAccountIdentifier extends AccountIdentifier {
    private final Type type = SORT_CODE_ACCOUNT_NUMBER;

    private String sortCode;

    private String accountNumber;
}
