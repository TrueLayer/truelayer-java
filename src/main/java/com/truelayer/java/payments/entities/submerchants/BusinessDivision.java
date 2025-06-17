package com.truelayer.java.payments.entities.submerchants;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class BusinessDivision extends UltimateCounterparty {
    private final Type type = Type.BUSINESS_DIVISION;
    private String id;
    private String name;
}
