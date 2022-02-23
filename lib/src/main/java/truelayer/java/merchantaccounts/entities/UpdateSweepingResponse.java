package truelayer.java.merchantaccounts.entities;

import lombok.Value;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.merchantaccounts.entities.sweeping.Destination;
import truelayer.java.merchantaccounts.entities.sweeping.Frequency;

@Value
public class UpdateSweepingResponse {

    int maxAmountInMinor;

    CurrencyCode currency;

    Frequency frequency;

    Destination destination;
}
