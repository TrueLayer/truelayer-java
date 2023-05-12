package com.truelayer.java.payments.entities;

import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import java.util.List;
import lombok.Value;

@Value
public class ListPaymentRefundsResponse {
    List<PaymentRefund> items;
}
