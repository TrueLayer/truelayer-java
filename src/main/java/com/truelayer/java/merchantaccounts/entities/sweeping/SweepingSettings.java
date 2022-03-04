package com.truelayer.java.merchantaccounts.entities.sweeping;

import com.truelayer.java.entities.CurrencyCode;
import lombok.Value;

@Value
public class SweepingSettings {

    int maxAmountInMinor;

    CurrencyCode currency;

    Frequency frequency;

    Destination destination;
}
