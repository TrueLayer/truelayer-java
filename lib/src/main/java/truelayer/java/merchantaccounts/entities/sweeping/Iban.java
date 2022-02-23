package truelayer.java.merchantaccounts.entities.sweeping;

import lombok.EqualsAndHashCode;
import lombok.Value;

import static truelayer.java.merchantaccounts.entities.sweeping.Destination.Type.IBAN;

@Value
@EqualsAndHashCode(callSuper = false)
public class Iban extends Destination{
    Type type = IBAN;

    String iban;
}
