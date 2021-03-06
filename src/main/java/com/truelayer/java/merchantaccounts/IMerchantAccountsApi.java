package com.truelayer.java.merchantaccounts;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import com.truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Exposes all the merchant accounts related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/list-operating-accounts"><i>Merchant Accounts</i> API reference</a>
 */
public interface IMerchantAccountsApi {

    /**
     * List all your TrueLayer's merchant accounts. There might be more than one account per currency.
     * @return the list of all you TrueLayer's merchant accounts
     * @see <a href="https://docs.truelayer.com/reference/list-operating-accounts"><i>List Merchant Accounts</i> API reference</a>
     */
    @GET("/merchant-accounts")
    CompletableFuture<ApiResponse<ListMerchantAccountsResponse>> listMerchantAccounts();

    /**
     * Get the details of a single merchant account.
     * @param merchantAccountId the id of the merchant account
     * @return the details of the given merchant account
     * @see <a href="https://docs.truelayer.com/reference/get-operating-account"><i>Get Merchant Accounts</i> API reference</a>
     */
    @GET("/merchant-accounts/{merchantAccountId}")
    CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(
            @Path("merchantAccountId") String merchantAccountId);

    /**
     * Get the transactions of a single merchant account.
     * @param merchantAccountId the id of the merchant account
     * @param from Timestamp as a string for the start of the range you are querying. Mandatory
     * @param to Timestamp as a string for the end of the range you are querying. Mandatory
     * @param type Filter transactions by type. If omitted, both payments and payouts will be returned.
     * @return the list of transactions matching the specified filters
     * @see <a href="https://docs.truelayer.com/reference/get_merchant-accounts-id-transactions"><i>Get Transactions</i> API reference</a>
     */
    @GET("/merchant-accounts/{merchantAccountId}/transactions")
    CompletableFuture<ApiResponse<ListTransactionsResponse>> listTransactions(
            @Path("merchantAccountId") String merchantAccountId,
            @Query("from") String from,
            @Query("to") String to,
            @Query("type") TransactionTypeQuery type);

    /**
     * Set the automatic sweeping settings for a merchant account. At regular intervals, any available balance in excess
     * of the configured <code>max_amount_in_minor</code> is withdrawn to a pre-configured IBAN.
     * @param merchantAccountId the id of the merchant account
     * @param updateSweepingRequest the update/setup sweeping request
     * @return the updated sweeping settings
     * @see <a href="https://docs.truelayer.com/reference/post_merchant-accounts-id-sweeping"><i>Setup/Update Sweeping</i> API reference</a>
     */
    @POST("/merchant-accounts/{merchantAccountId}/sweeping")
    CompletableFuture<ApiResponse<SweepingSettings>> updateSweeping(
            @Path("merchantAccountId") String merchantAccountId, @Body UpdateSweepingRequest updateSweepingRequest);

    /**
     * Get the automatic sweeping settings for a merchant account.
     * @param merchantAccountId the id of the merchant account
     * @return the sweeping settings for the given merchant account
     * @see <a href="https://docs.truelayer.com/reference/get_merchant-accounts-id-sweeping"><i>Get Sweeping Settings</i> API reference</a>
     */
    @GET("/merchant-accounts/{merchantAccountId}/sweeping")
    CompletableFuture<ApiResponse<SweepingSettings>> getSweepingSettings(
            @Path("merchantAccountId") String merchantAccountId);

    /**
     * Disable automatic sweeping for a merchant account.
     * @param merchantAccountId the id of the merchant account
     * @return an empty response in case of success
     * @see <a href="https://docs.truelayer.com/reference/delete_merchant-accounts-id-sweeping"><i>Disable Sweeping</i> API reference</a>
     */
    @DELETE("/merchant-accounts/{merchantAccountId}/sweeping")
    CompletableFuture<ApiResponse<Void>> disableSweeping(@Path("merchantAccountId") String merchantAccountId);

    @GET("/merchant-accounts/{merchantAccountId}/payment-sources")
    CompletableFuture<ApiResponse<ListPaymentSourcesResponse>> listPaymentSources(
            @Path("merchantAccountId") String merchantAccountId, @Query("user_id") String userId);
}
