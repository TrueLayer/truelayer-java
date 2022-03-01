package truelayer.java.merchantaccounts.entities.sweeping;

import static truelayer.java.merchantaccounts.entities.sweeping.Destination.Type.IBAN;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class Iban extends Destination {
    Type type = IBAN;

    String iban;
}
