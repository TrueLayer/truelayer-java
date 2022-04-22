package com.truelayer.java.payments.entities.paymentmethod;

import static com.truelayer.java.payments.entities.paymentmethod.PaymentMethod.Type.MANDATE;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Mandate extends PaymentMethod {
    private final Type type = MANDATE;

    private String mandateId;
}
