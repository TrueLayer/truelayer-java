package com.truelayer.java.payouts.entities.beneficiary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PaymentSource extends Beneficiary {
    private final Type type = Type.PAYMENT_SOURCE;

    private String reference;
    private String paymentSourceId;
    private String userId;
}
