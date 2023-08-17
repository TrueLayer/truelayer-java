package com.truelayer.java.merchantaccounts;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.IAuthenticatedHandler;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

@Value
public class MerchantAccountsHandler implements IAuthenticatedHandler, IMerchantAccountsHandler {
    IMerchantAccountsApi merchantAccountsApi;

    @Override
    public RequestScopes getRequestScopes() {
        return RequestScopes.builder().scope(PAYMENTS).build();
    }

    @Override
    public CompletableFuture<ApiResponse<ListMerchantAccountsResponse>> listMerchantAccounts() {
        return merchantAccountsApi.listMerchantAccounts(getRequestScopes());
    }

    @Override
    public CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(String merchantAccountId) {
        return merchantAccountsApi.getMerchantAccountById(getRequestScopes(), merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<ListTransactionsResponse>> listTransactions(
            String merchantAccountId, ListTransactionsQuery query) {
        return merchantAccountsApi.listTransactions(
                getRequestScopes(),
                merchantAccountId,
                DateTimeFormatter.ISO_ZONED_DATE_TIME.format(query.from()),
                DateTimeFormatter.ISO_ZONED_DATE_TIME.format(query.to()),
                query.type());
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            String merchantAccountId, UpdateSweepingRequest updateSweepingRequest) {
        return merchantAccountsApi.updateSweeping(
                getRequestScopes(), emptyMap(), merchantAccountId, updateSweepingRequest);
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            Headers headers, String merchantAccountId, UpdateSweepingRequest updateSweepingRequest) {
        return merchantAccountsApi.updateSweeping(
                getRequestScopes(), toMap(headers), merchantAccountId, updateSweepingRequest);
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> getSweepingSettings(String merchantAccountId) {
        return merchantAccountsApi.getSweepingSettings(getRequestScopes(), merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> disableSweeping(String merchantAccountId) {
        return merchantAccountsApi.disableSweeping(getRequestScopes(), emptyMap(), merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> disableSweeping(Headers headers, String merchantAccountId) {
        return merchantAccountsApi.disableSweeping(getRequestScopes(), toMap(headers), merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<ListPaymentSourcesResponse>> listPaymentSources(
            String merchantAccountId, ListPaymentSourcesQuery query) {
        return merchantAccountsApi.listPaymentSources(getRequestScopes(), merchantAccountId, query.userId());
    }
}
