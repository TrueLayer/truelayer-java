package com.truelayer.java.payouts;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.IAuthenticatedHandler;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import com.truelayer.java.payouts.entities.CreatePayoutResponse;
import com.truelayer.java.payouts.entities.Payout;
import java.util.concurrent.CompletableFuture;
import lombok.Builder;

@Builder
public class PayoutsHandler implements IAuthenticatedHandler, IPayoutsHandler {

    private IPayoutsApi payoutsApi;

    @Builder.Default
    private RequestScopes scopes = RequestScopes.builder().scope(PAYMENTS).build();

    @Override
    public RequestScopes getRequestScopes() {
        return scopes;
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(CreatePayoutRequest request) {
        return payoutsApi.createPayout(getRequestScopes(), emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(
            Headers headers, CreatePayoutRequest request) {
        return payoutsApi.createPayout(getRequestScopes(), toMap(headers), request);
    }

    @Override
    public CompletableFuture<ApiResponse<Payout>> getPayout(String payoutId) {
        return payoutsApi.getPayout(getRequestScopes(), payoutId);
    }
}
