package com.truelayer.java.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AvailabilityRecommendedStatus {
    HEALTHY("healthy"),
    UNHEALTHY("unhealthy");

    @JsonValue
    private final String recommendedStatus;
}
