package com.truelayer.java.paymentsproviders;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Exposes all the payments providers related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/get-payment-provider"><i>Payments Providers</i> API reference</a>
 */
public interface IPaymentsProvidersApi {

    /**
     * Gets a payment provider resource by id.
     *
     * @param providerId the provider identifier
     * @return the response of the <i>Get Payments Provider</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-provider"><i>Get Payment Provider</i> API reference</a>
     */
    @GET("/payments-providers/{id}")
    CompletableFuture<ApiResponse<PaymentsProvider>> getProvider(
            @Path("id") String providerId, @Query("client_id") String clientId);
}
