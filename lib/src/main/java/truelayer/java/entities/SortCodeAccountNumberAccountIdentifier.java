package truelayer.java.entities;

import static truelayer.java.entities.AccountIdentifier.Type.*;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SortCodeAccountNumberAccountIdentifier extends AccountIdentifier {
    private final Type type = SORT_CODE_ACCOUNT_NUMBER;

    private String sortCode;

    private String accountNumber;
}
