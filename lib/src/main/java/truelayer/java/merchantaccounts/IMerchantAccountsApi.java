package truelayer.java.merchantaccounts;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import retrofit2.http.Path;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountsResponse;
import truelayer.java.merchantaccounts.entities.MerchantAccount;

/**
 * Exposes all the merchant accounts related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/list-operating-accounts"><i>Merchant Accounts</i> API reference</a>
 */
public interface IMerchantAccountsApi {

    @GET("/merchant-accounts")
    CompletableFuture<ApiResponse<ListMerchantAccountsResponse>> listMerchantAccounts();

    @GET("/merchant-accounts/{id}")
    CompletableFuture<ApiResponse<MerchantAccount>> getMerchantAccountById(@Path("id") String id);
}
