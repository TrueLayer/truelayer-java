package com.truelayer.java.paymentsproviders.entities;

import lombok.*;

@Value
public class MandatesCapabilities {
    VrpSweepingCapabilities vrpSweeping;

    VrpCommercialCapabilities vrpCommercial;
}
