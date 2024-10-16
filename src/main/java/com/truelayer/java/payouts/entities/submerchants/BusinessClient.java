package com.truelayer.java.payouts.entities.submerchants;

import com.truelayer.java.entities.Address;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusinessClient extends UltimateCounterparty {
    private final Type type = Type.BUSINESS_CLIENT;
    private String tradingName;
    private String commercialName;
    private String url;
    private String mcc;
    private String registrationNumber;
    private Address address;
}
