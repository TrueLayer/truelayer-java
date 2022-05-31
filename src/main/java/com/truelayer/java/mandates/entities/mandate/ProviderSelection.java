package com.truelayer.java.mandates.entities.mandate;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ProviderSelection {
    final String type = "preselected";

    String providerId;

    // todo:remitter
}
