package truelayer.java.entities;

import static truelayer.java.entities.AccountIdentifier.Type.BBAN;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BbanAccountIdentifier extends AccountIdentifier {
    private final Type type = BBAN;

    private String bban;
}
