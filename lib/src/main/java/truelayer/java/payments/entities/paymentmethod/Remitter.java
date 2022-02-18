package truelayer.java.payments.entities.paymentmethod;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.entities.AccountIdentifier;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Remitter {
    private AccountIdentifier accountIdentifier;

    private String accountHolderName;
}
