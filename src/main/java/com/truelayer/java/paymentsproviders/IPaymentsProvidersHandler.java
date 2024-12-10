package com.truelayer.java.paymentsproviders;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import com.truelayer.java.paymentsproviders.entities.searchproviders.SearchPaymentProvidersRequest;
import com.truelayer.java.paymentsproviders.entities.searchproviders.SearchPaymentProvidersResponse;
import java.util.concurrent.CompletableFuture;

/**
 * Exposes all the payments providers related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/get-payment-provider"><i>Payments Providers</i> API reference</a>
 */
public interface IPaymentsProvidersHandler {

    /**
     * Gets a payment provider resource by id.
     *
     * @param providerId the provider identifier
     * @return the response of the <i>Get Payments Provider</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-provider"><i>Get Payment Provider</i> API reference</a>
     */
    CompletableFuture<ApiResponse<PaymentsProvider>> getProvider(String providerId);

    /**
     * Returns a list of payments providers.
     *
     * @param request the request with filters to search providers
     * @return the response of the <i>Search Payments Providers</i> operation
     * @see <a href="https://docs.truelayer.com/reference/search-payment-providers"><i>Search Payment Providers</i> API reference</a>
     */
    CompletableFuture<ApiResponse<SearchPaymentProvidersResponse>> searchProviders(
            SearchPaymentProvidersRequest request);
}
