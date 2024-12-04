package com.truelayer.java.paymentsproviders.entities;

import java.util.List;
import lombok.Value;

@Value
public class SearchPaymentProvidersResponse {
    List<PaymentsProvider> items;
}
