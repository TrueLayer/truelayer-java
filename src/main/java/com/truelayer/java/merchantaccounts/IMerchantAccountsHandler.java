package com.truelayer.java.merchantaccounts;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

public interface IMerchantAccountsHandler {

    CompletableFuture<ApiResponse<ListMerchantAccountsResponse>> listMerchantAccounts();

    CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(String merchantAccountId);

    CompletableFuture<ApiResponse<ListTransactionsResponse>> listTransactions(
            String merchantAccountId, ListTransactionsQuery query);

    /**
     * Overload meant to give clients created before December 2023 the ability to opt-in for paginated results at a <i>request</i> level via the
     * <code>tl-enable-pagination</code> HTTP header.<br>
     * This is useful for clients that want to ease the migration towards paginated responses, it is not required once
     * the pagination opt-in is enabled at the client level.<br>
     * See <a href="https://docs.truelayer.com/docs/enable-pagination-transactions-endpoint#enable-pagination">Enable pagination</a> for more details.<br><br>
     * Clients created after November 2023 can use the {@link #listTransactions(String, ListTransactionsQuery) simpler overload}.
     * @param headers map representing custom HTTP headers to be sent. In this case used to specify the {@link Headers#enablePagination enablePagination} flag
     * @param merchantAccountId the id of the merchant account
     * @param query the query to filter the transactions
     * @return a list of transactions matching the specified filters
     */
    CompletableFuture<ApiResponse<ListTransactionsResponse>> listTransactions(
            Headers headers, String merchantAccountId, ListTransactionsQuery query);

    CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            String merchantAccountId, UpdateSweepingRequest updateSweepingRequest);

    CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            Headers headers, String merchantAccountId, UpdateSweepingRequest updateSweepingRequest);

    CompletableFuture<ApiResponse<SweepingSettings>> getSweepingSettings(String merchantAccountId);

    CompletableFuture<ApiResponse<Void>> disableSweeping(String merchantAccountId);

    CompletableFuture<ApiResponse<Void>> disableSweeping(Headers headers, String merchantAccountId);

    CompletableFuture<ApiResponse<ListPaymentSourcesResponse>> listPaymentSources(
            String merchantAccountId, ListPaymentSourcesQuery query);
}
