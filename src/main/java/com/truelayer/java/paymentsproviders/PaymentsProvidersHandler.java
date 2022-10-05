package com.truelayer.java.paymentsproviders;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

/**
 * {@inheritDoc}
 */
@Value
public class PaymentsProvidersHandler implements IPaymentsProvidersHandler {

    String clientId;

    IPaymentsProvidersApi paymentsProvidersApi;

    public static PaymentsProvidersHandlerBuilder New() {
        return new PaymentsProvidersHandlerBuilder();
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentsProvider>> getProvider(String providerId) {
        return paymentsProvidersApi.getProvider(providerId, clientId);
    }
}
