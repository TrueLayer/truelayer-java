package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import lombok.EqualsAndHashCode;
import lombok.Value;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.SORT_CODE_ACCOUNT_NUMBER;

@EqualsAndHashCode(callSuper = true)
@Value
public class SortCodeAccountNumber extends SchemeIdentifier {
    Type type = SORT_CODE_ACCOUNT_NUMBER;

    String sortCode;

    String accountNumber;
}
