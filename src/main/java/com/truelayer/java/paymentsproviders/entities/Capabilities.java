package com.truelayer.java.paymentsproviders.entities;

import lombok.Value;

@Value
public class Capabilities {
    PaymentsCapabilities payments;

    MandatesCapabilities mandates;
}
