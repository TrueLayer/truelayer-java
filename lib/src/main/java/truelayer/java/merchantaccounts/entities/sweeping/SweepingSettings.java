package truelayer.java.merchantaccounts.entities.sweeping;

import lombok.Value;
import truelayer.java.entities.CurrencyCode;

@Value
public class SweepingSettings {

    int maxAmountInMinor;

    CurrencyCode currency;

    Frequency frequency;

    Destination destination;
}
