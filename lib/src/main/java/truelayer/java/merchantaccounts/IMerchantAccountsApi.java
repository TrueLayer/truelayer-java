package truelayer.java.merchantaccounts;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.GetTransactionsResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountsResponse;
import truelayer.java.merchantaccounts.entities.MerchantAccount;
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
     */
    @GET("/merchant-accounts/{merchantAccountId}")
    CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(@Path("id") String merchantAccountId);

    /**
     * Get the transactions of a single merchant account.
     * @param merchantAccountId the id of the merchant account
     * @param from Timestamp as a string for the start of the range you are querying. Mandatory
     * @param to Timestamp as a string for the end of the range you are querying. Mandatory
     * @param type Filter transactions by type. If omitted, both payments and payouts will be returned.
     * @return
     */
    @GET("/merchant-accounts/{merchantAccountId}/transactions")
    CompletableFuture<ApiResponse<GetTransactionsResponse>> getTransactions(
            @Path("id") String merchantAccountId,
            @Query("from") String from,
            @Query("to") String to,
            @Query("type") TransactionTypeQuery type);
}
