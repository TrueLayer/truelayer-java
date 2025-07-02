package com.truelayer.java.payments.entities.submerchants;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SubMerchants {
    private UltimateCounterparty ultimateCounterparty;
}
