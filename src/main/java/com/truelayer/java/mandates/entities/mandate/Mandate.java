package com.truelayer.java.mandates.entities.mandate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@ToString
@EqualsAndHashCode
public abstract class Mandate {

    public static VRPSweepingMandate.VRPSweepingMandateBuilder vrpSweepingMandate() {
        return VRPSweepingMandate.builder();
    }

    public static VRPCommercialMandate.VRPCommercialMandateBuilder vrpCommercialMandate() {
        return VRPCommercialMandate.builder();
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        SWEEPING("sweeping"),
        COMMERCIAL("commercial");

        @JsonValue
        private final String type;
    }
}
