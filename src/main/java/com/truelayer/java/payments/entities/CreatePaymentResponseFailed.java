package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentdetail.Status;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class CreatePaymentResponseFailed extends CreatePaymentResponse {
    Status status = Status.FAILED;

    String failureStage;

    String failureReason;
}
