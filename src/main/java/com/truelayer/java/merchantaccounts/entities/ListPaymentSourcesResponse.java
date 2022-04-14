package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.merchantaccounts.entities.paymentsources.PaymentSource;
import java.util.List;
import lombok.Value;

@Value
public class ListPaymentSourcesResponse {

    List<PaymentSource> items;
}
