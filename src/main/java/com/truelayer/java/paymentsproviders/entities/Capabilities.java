package com.truelayer.java.paymentsproviders.entities;

import lombok.*;

@Value
public class Capabilities {
    PaymentsCapabilities payments;

    MandatesCapabilities mandates;
}
