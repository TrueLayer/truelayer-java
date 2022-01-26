package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import lombok.EqualsAndHashCode;
import lombok.Value;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.BBAN;
import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.IBAN;

@EqualsAndHashCode(callSuper = true)
@Value
public class Bban extends SchemeIdentifier {
    Type type = BBAN;

    String bban;
}
