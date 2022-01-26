package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import lombok.EqualsAndHashCode;
import lombok.Value;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.IBAN;
import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.SORT_CODE_ACCOUNT_NUMBER;

@EqualsAndHashCode(callSuper = true)
@Value
public class Iban extends SchemeIdentifier {
    Type type = IBAN;

    String iban;
}
