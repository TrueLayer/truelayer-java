package truelayer.java.merchantaccounts;

import java.util.concurrent.CompletableFuture;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountResponse;

/**
 * Exposes all the merchant-accounts related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/list-operating-accounts"><i>Merchant Accounts</i> API reference</a>
 */
public interface IMerchantAccountsHandler {

    /**
     * List all the merchant accounts associated to the used client.
     * @return the response of the <i>List merchant accounts</i> operation
     */
    CompletableFuture<ApiResponse<ListMerchantAccountResponse>> listMerchantAccounts();
}
