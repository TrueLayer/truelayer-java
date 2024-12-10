package com.truelayer.java.paymentsproviders.entities.searchproviders;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class MandatesCapabilities {
    VrpSweepingCapabilities vrpSweeping;

    VrpCommercialCapabilities vrpCommercial;
}
