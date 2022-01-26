package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import lombok.EqualsAndHashCode;
import lombok.Value;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.BBAN;
import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.NRB;

@EqualsAndHashCode(callSuper = true)
@Value
public class Nrb extends SchemeIdentifier {
    Type type = NRB;

    String nrb;
}
