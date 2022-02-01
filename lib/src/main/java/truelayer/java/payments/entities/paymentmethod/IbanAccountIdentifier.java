package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.payments.entities.paymentmethod.AccountIdentifier.Type.IBAN;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IbanAccountIdentifier extends AccountIdentifier {
    private final Type type = IBAN;

    private String iban;
}
