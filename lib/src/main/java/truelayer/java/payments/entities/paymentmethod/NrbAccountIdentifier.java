package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.payments.entities.paymentmethod.AccountIdentifier.Type.NRB;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NrbAccountIdentifier extends AccountIdentifier {
    private final Type type = NRB;

    private String nrb;
}
