package truelayer.java.entities.accountidentifier;

import static truelayer.java.entities.accountidentifier.AccountIdentifier.Type.IBAN;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IbanAccountIdentifier extends AccountIdentifier {
    private final Type type = IBAN;

    private String iban;
}
