package com.truelayer.java.payments.entities.paymentrefund;

import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class FailedPaymentRefund extends PaymentRefund {
    Status status = Status.FAILED;
    ZonedDateTime failedAt;
    String failureReason;
}
