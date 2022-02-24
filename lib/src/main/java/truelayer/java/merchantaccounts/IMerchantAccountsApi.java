package truelayer.java.merchantaccounts;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.*;
import truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;

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
    CompletableFuture<ApiResponse<GetTransactionsResponse>> getTransactions(
            @Path("merchantAccountId") String merchantAccountId,
            @Query("from") String from,
            @Query("to") String to,
            @Query("type") TransactionTypeQuery type);

    /**
     * Set the automatic sweeping settings for a merchant account. At regular intervals, any available balance in excess
     * of the configured <code>max_amount_in_minor</code> is withdrawn to a pre-configured IBAN.
     * @param merchantAccountId the id of the merchant account
     * @param updateSweepingRequest the update/setup sweeping request
     * @return the response of the setup/update operation
     * @see <a href="https://docs.truelayer.com/reference/post_merchant-accounts-id-sweeping"><i>Setup/Update Sweeping</i> API reference</a>
     */
    @POST("/merchant-accounts/{merchantAccountId}/sweeping")
    CompletableFuture<ApiResponse<UpdateSweepingResponse>> updateSweeping(
            @Path("merchantAccountId") String merchantAccountId, @Body UpdateSweepingRequest updateSweepingRequest);
}
