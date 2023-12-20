package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
public class SettlementRisk {
    Category category;

    @RequiredArgsConstructor
    @Getter
    public enum Category {
        LOW_RISK("low_risk"),
        HIGH_RISK("high_risk");

        @JsonValue
        private final String category;
    }
}
