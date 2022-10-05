package com.truelayer.java.paymentsproviders;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface that models /payments-providers/* endpoints
 */
public interface IPaymentsProvidersApi {

    @GET("/payments-providers/{id}")
    CompletableFuture<ApiResponse<PaymentsProvider>> getProvider(
            @Path("id") String providerId, @Query("client_id") String clientId);
}
