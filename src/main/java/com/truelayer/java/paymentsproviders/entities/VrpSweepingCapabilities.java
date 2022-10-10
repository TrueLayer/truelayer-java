package com.truelayer.java.paymentsproviders.entities;

import com.truelayer.java.payments.entities.ReleaseChannel;
import lombok.Value;

@Value
public class VrpSweepingCapabilities {
    ReleaseChannel releaseChannel;
}
