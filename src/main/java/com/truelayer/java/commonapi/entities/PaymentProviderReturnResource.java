package com.truelayer.java.commonapi.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PaymentProviderReturnResource extends ProviderReturnResource {
    Type type = Type.PAYMENT;

    String paymentId;
}
