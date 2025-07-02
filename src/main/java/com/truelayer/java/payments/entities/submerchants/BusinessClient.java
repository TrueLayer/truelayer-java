package com.truelayer.java.payments.entities.submerchants;

import com.truelayer.java.entities.Address;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class BusinessClient extends UltimateCounterparty {
    private final Type type = Type.BUSINESS_CLIENT;
    private String tradingName;
    private String commercialName;
    private String url;
    private String mcc;
    private String registrationNumber;
    private Address address;
}
