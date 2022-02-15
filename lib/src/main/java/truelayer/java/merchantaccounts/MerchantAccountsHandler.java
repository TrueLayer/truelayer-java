package truelayer.java.merchantaccounts;

import java.util.concurrent.CompletableFuture;
import lombok.Value;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountResponse;

/**
 * @inheritDoc
 */
@Value
public class MerchantAccountsHandler implements IMerchantAccountsHandler {

    IMerchantAccountsApi merchantAccountsApi;

    public static MerchantAccountsHandlerBuilder New() {
        return new MerchantAccountsHandlerBuilder();
    }

    @Override
    public CompletableFuture<ApiResponse<ListMerchantAccountResponse>> listMerchantAccounts() {
        return merchantAccountsApi.listMerchantAccounts();
    }
}
