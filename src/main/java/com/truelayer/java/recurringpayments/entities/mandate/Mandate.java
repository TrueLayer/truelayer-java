package com.truelayer.java.recurringpayments.entities.mandate;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class Mandate {

    public static VRPSweepingMandate.VRPSweepingMandateBuilder vrpSweepingMandate() {
        return new VRPSweepingMandate.VRPSweepingMandateBuilder();
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        SWEEPING("sweeping");

        @JsonValue
        private final String type;
    }
}
