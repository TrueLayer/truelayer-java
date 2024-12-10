package com.truelayer.java.paymentsproviders;

import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import com.truelayer.java.paymentsproviders.entities.searchproviders.SearchPaymentProvidersRequest;
import com.truelayer.java.paymentsproviders.entities.searchproviders.SearchPaymentProvidersResponse;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

/**
 * Interface that models /payments-providers/* endpoints
 */
public interface IPaymentsProvidersApi {

    @GET("/payments-providers/{id}")
    CompletableFuture<ApiResponse<PaymentsProvider>> getProvider(
            @Tag RequestScopes scopes, @Path("id") String providerId);

    @POST("/payments-providers/search")
    CompletableFuture<ApiResponse<SearchPaymentProvidersResponse>> searchPaymentProviders(
            @Tag RequestScopes scopes, @Body SearchPaymentProvidersRequest request);
}
