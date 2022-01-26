package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.IBAN;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class Iban extends SchemeIdentifier {
    Type type = IBAN;

    String iban;
}
