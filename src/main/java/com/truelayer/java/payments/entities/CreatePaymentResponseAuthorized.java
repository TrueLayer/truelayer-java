package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CreatePaymentResponseAuthorized extends CreatePaymentResponse {
    Status status = Status.AUTHORIZED;
}
