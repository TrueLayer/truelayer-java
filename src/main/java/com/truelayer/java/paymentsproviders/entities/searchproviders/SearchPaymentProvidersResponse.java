package com.truelayer.java.paymentsproviders.entities.searchproviders;

import java.util.List;

import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import lombok.Value;

@Value
public class SearchPaymentProvidersResponse {
    List<PaymentsProvider> items;
}
