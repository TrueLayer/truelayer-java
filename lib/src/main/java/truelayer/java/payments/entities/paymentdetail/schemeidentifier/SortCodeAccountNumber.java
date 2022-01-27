package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.SORT_CODE_ACCOUNT_NUMBER;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = false)
@Value
public class SortCodeAccountNumber extends SchemeIdentifier {
    Type type = SORT_CODE_ACCOUNT_NUMBER;

    String sortCode;

    String accountNumber;
}
