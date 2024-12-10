package com.truelayer.java.paymentsproviders;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;

import com.truelayer.java.IAuthenticatedHandler;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import com.truelayer.java.paymentsproviders.entities.searchproviders.SearchPaymentProvidersRequest;
import com.truelayer.java.paymentsproviders.entities.searchproviders.SearchPaymentProvidersResponse;
import java.util.concurrent.CompletableFuture;
import lombok.Builder;

/**
 * {@inheritDoc}
 */
@Builder
public class PaymentsProvidersHandler implements IAuthenticatedHandler, IPaymentsProvidersHandler {

    IPaymentsProvidersApi paymentsProvidersApi;

    @Builder.Default
    private RequestScopes scopes = RequestScopes.builder().scope(PAYMENTS).build();

    @Override
    public RequestScopes getRequestScopes() {
        return scopes;
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentsProvider>> getProvider(String providerId) {
        return paymentsProvidersApi.getProvider(getRequestScopes(), providerId);
    }

    @Override
    public CompletableFuture<ApiResponse<SearchPaymentProvidersResponse>> searchProviders(
            SearchPaymentProvidersRequest request) {
        return paymentsProvidersApi.searchPaymentProviders(getRequestScopes(), request);
    }
}
