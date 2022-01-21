package truelayer.java.payments.entities.paymentmethod;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Remitter {
    private BasePaymentSchemeIdentifier schemeIdentifier;

    private String name;
}
