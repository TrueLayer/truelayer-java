package truelayer.java.entities.accountidentifier;

import static truelayer.java.entities.accountidentifier.AccountIdentifier.Type.NRB;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NrbAccountIdentifier extends AccountIdentifier {
    private final Type type = NRB;

    private String nrb;
}
