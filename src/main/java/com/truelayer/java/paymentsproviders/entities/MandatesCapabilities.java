package com.truelayer.java.paymentsproviders.entities;

import lombok.Value;

@Value
public class MandatesCapabilities {
    VrpSweepingCapabilities vrpSweeping;

    VrpCommercialCapabilities vrpCommercial;
}
