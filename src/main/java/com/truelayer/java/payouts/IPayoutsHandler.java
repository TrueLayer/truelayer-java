package com.truelayer.java.payouts;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import com.truelayer.java.payouts.entities.CreatePayoutResponse;
import com.truelayer.java.payouts.entities.Payout;
import java.util.concurrent.CompletableFuture;

/**
 * Provides /payouts API integration without the burden of Retrofit's annotation
 * and improve both usability and backward compatibility for the implemented endpoints.
 */
public interface IPayoutsHandler {

    CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(CreatePayoutRequest request);

    CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(Headers headers, CreatePayoutRequest request);

    CompletableFuture<ApiResponse<Payout>> getPayout(String payoutId);
}
