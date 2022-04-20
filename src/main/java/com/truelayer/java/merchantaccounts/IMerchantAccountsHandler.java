package com.truelayer.java.merchantaccounts;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

public interface IMerchantAccountsHandler {

    CompletableFuture<ApiResponse<ListMerchantAccountsResponse>> listMerchantAccounts();

    CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(String merchantAccountId);

    CompletableFuture<ApiResponse<ListTransactionsResponse>> listTransactions(
            String merchantAccountId, ListTransactionsQuery query);

    CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            String merchantAccountId, UpdateSweepingRequest updateSweepingRequest);

    CompletableFuture<ApiResponse<SweepingSettings>> getSweepingSettings(String merchantAccountId);

    CompletableFuture<ApiResponse<Void>> disableSweeping(String merchantAccountId);

    CompletableFuture<ApiResponse<ListPaymentSourcesResponse>> listPaymentSources(
            String merchantAccountId, ListPaymentSourcesQuery query);
}
