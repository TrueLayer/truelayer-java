package truelayer.java.payments.entities.paymentmethod;

import static truelayer.java.payments.entities.paymentmethod.AccountIdentifier.Type.BBAN;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BbanAccountIdentifier extends AccountIdentifier {
    private final Type type = BBAN;

    private String bban;
}
