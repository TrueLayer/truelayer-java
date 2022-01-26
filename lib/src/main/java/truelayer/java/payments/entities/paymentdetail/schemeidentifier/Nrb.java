package truelayer.java.payments.entities.paymentdetail.schemeidentifier;

import static truelayer.java.payments.entities.paymentdetail.schemeidentifier.SchemeIdentifier.Type.NRB;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class Nrb extends SchemeIdentifier {
    Type type = NRB;

    String nrb;
}
