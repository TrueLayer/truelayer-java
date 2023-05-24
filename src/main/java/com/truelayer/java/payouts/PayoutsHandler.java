package com.truelayer.java.payouts;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import com.truelayer.java.payouts.entities.CreatePayoutResponse;
import com.truelayer.java.payouts.entities.Payout;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

@Value
public class PayoutsHandler implements IPayoutsHandler {

    IPayoutsApi payoutsApi;

    @Override
    public CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(CreatePayoutRequest request) {
        return payoutsApi.createPayout(emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(
            Headers headers, CreatePayoutRequest request) {
        return payoutsApi.createPayout(toMap(headers), request);
    }

    @Override
    public CompletableFuture<ApiResponse<Payout>> getPayout(String payoutId) {
        return payoutsApi.getPayout(payoutId);
    }
}
