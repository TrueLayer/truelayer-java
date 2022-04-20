package com.truelayer.java.merchantaccounts;

import com.truelayer.java.http.entities.ApiResponse;
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
        return merchantAccountsApi.listTransactions(
                merchantAccountId,
                DateTimeFormatter.ISO_DATE_TIME.format(query.from()),
                DateTimeFormatter.ISO_ZONED_DATE_TIME.format(query.to()),
                query.type());
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            String merchantAccountId, UpdateSweepingRequest updateSweepingRequest) {
        return merchantAccountsApi.updateSweeping(merchantAccountId, updateSweepingRequest);
    }

    @Override
    public CompletableFuture<ApiResponse<SweepingSettings>> getSweepingSettings(String merchantAccountId) {
        return merchantAccountsApi.getSweepingSettings(merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> disableSweeping(String merchantAccountId) {
        return merchantAccountsApi.disableSweeping(merchantAccountId);
    }

    @Override
    public CompletableFuture<ApiResponse<ListPaymentSourcesResponse>> listPaymentSources(
            String merchantAccountId, ListPaymentSourcesQuery query) {
        return merchantAccountsApi.listPaymentSources(merchantAccountId, query.userId());
    }
}
