package truelayer.java.entities;

import static truelayer.java.entities.AccountIdentifier.Type.NRB;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NrbAccountIdentifier extends AccountIdentifier {
    private final Type type = NRB;

    private String nrb;
}
