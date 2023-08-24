package com.truelayer.java.merchantaccounts;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

@Value
public class MerchantAccountsHandler implements IMerchantAccountsHandler {

    IMerchantAccountsApi merchantAccountsApi;

    @Override
    public CompletableFuture<ApiResponse<ListMerchantAccountsResponse>> listMerchantAccounts() {
        return merchantAccountsApi.listMerchantAccounts();
    }

    @Override
    public CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(String merchantAccountId) {
        return merchantAccountsApi.getMerchantAccountById(merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<ListTransactionsResponse>> listTransactions(
            String merchantAccountId, ListTransactionsQuery query) {
        String from = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(query.from());
        String to = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(query.to());
        return merchantAccountsApi.listTransactions(merchantAccountId, from, to, query.type());
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            String merchantAccountId, UpdateSweepingRequest updateSweepingRequest) {
        return merchantAccountsApi.updateSweeping(emptyMap(), merchantAccountId, updateSweepingRequest);
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            Headers headers, String merchantAccountId, UpdateSweepingRequest updateSweepingRequest) {
        return merchantAccountsApi.updateSweeping(toMap(headers), merchantAccountId, updateSweepingRequest);
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> getSweepingSettings(String merchantAccountId) {
        return merchantAccountsApi.getSweepingSettings(merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> disableSweeping(String merchantAccountId) {
        return merchantAccountsApi.disableSweeping(emptyMap(), merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> disableSweeping(Headers headers, String merchantAccountId) {
        return merchantAccountsApi.disableSweeping(toMap(headers), merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<ListPaymentSourcesResponse>> listPaymentSources(
            String merchantAccountId, ListPaymentSourcesQuery query) {
        return merchantAccountsApi.listPaymentSources(merchantAccountId, query.userId());
    }
}
