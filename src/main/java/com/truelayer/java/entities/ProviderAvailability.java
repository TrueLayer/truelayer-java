package com.truelayer.java.entities;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ProviderAvailability {
    AvailabilityRecommendedStatus recommendedStatus;
    ZonedDateTime updatedAt;
}
