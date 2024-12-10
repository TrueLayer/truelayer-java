package com.truelayer.java.paymentsproviders.entities.searchproviders;

import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import java.util.List;
import lombok.Value;

@Value
public class SearchPaymentProvidersResponse {
    List<PaymentsProvider> items;
}
