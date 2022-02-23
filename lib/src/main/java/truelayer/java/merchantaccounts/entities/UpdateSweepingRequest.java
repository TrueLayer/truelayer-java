package truelayer.java.merchantaccounts.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.merchantaccounts.entities.sweeping.Frequency;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class UpdateSweepingRequest {
    private int maxAmountInMinor;

    private CurrencyCode currency;

    private Frequency frequency;
}
