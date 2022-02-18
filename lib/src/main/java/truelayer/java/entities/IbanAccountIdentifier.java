package truelayer.java.entities;

import static truelayer.java.entities.AccountIdentifier.Type.IBAN;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IbanAccountIdentifier extends AccountIdentifier {
    private final Type type = IBAN;

    private String iban;
}
