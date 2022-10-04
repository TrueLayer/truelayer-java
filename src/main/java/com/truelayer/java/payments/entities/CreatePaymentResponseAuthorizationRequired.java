package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CreatePaymentResponseAuthorizationRequired extends CreatePaymentResponse {
    Status status = Status.AUTHORIZATION_REQUIRED;
}
