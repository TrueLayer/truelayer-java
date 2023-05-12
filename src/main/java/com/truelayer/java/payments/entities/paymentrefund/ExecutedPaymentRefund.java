package com.truelayer.java.payments.entities.paymentrefund;

import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPaymentRefund extends PaymentRefund {
    Status status = Status.EXECUTED;
    ZonedDateTime executedAt;
}
