package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.BBAN;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class Bban extends SchemeIdentifier {
    Type type = BBAN;

    String bban;
}
