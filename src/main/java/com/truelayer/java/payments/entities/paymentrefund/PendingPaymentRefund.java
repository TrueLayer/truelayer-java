package com.truelayer.java.payments.entities.paymentrefund;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PendingPaymentRefund extends PaymentRefund {
    Status status = Status.PENDING;
}
