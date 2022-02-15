package truelayer.java.merchantaccounts;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.GET;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountResponse;
import truelayer.java.payments.entities.*;

/**
 * Interface that models /merchant-accounts/* endpoints
 */
public interface IMerchantAccountsApi {

    @GET("/merchant-accounts")
    CompletableFuture<ApiResponse<ListMerchantAccountResponse>> listMerchantAccounts();
}
