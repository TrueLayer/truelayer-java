package truelayer.java.acceptance;

import com.truelayer.java.TestUtils;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.ListMerchantAccountsResponse;
import com.truelayer.java.merchantaccounts.entities.MerchantAccount;
import com.truelayer.java.merchantaccounts.entities.transactions.Transaction;
import java.util.List;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountsResponse;
import truelayer.java.merchantaccounts.entities.transactions.Transaction;

@DisplayName("Merchant accounts acceptance tests")
public class MerchantAccountsAcceptanceTests extends AcceptanceTests {

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of merchant accounts for the given client")
    public void itShouldGetTheListOfMerchantAccounts() {
        ApiResponse<ListMerchantAccountsResponse> merchantAccountsResponse =
                tlClient.merchantAccounts().listMerchantAccounts().get();

        assertNotError(merchantAccountsResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the a merchant accounts by id")
    public void itShouldGetAMerchantAccountById() {
        ApiResponse<truelayer.java.merchantaccounts.entities.MerchantAccount> getMerchantAccountByIdResponse =
                tlClient.merchantAccounts()
                        .getMerchantAccountById(getMerchantAccount().getId())
                        .get();

        assertNotError(getMerchantAccountByIdResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of transactions for a given merchant account")
    public void itShouldGetTheListOfTransactions() {
        assertNotError(getTransactions());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the sweeping settings for the given merchant account")
    public void itShouldGetTheSweepingSettings() {
        // safely create a sweeping setting first to avoid 404 while getting
        UpdateSweepingRequest updateSweepingRequest = UpdateSweepingRequest.builder()
                .maxAmountInMinor(100)
                .frequency(Frequency.DAILY)
                .currency(CurrencyCode.GBP)
                .build();
        ApiResponse<SweepingSettings> updateSweepingResponse = tlClient.merchantAccounts()
                .updateSweeping(getMerchantAccount().getId(), updateSweepingRequest)
                .get();
        assertNotError(updateSweepingResponse);

        ApiResponse<SweepingSettings> getSweepingSettingsResponse = tlClient.merchantAccounts()
                .getSweepingSettings(getMerchantAccount().getId())
                .get();

        assertNotError(getSweepingSettingsResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should update the sweeping settings for the given merchant account")
    public void itShouldUpdateTheSweepingSettings() {
        UpdateSweepingRequest updateSweepingRequest = UpdateSweepingRequest.builder()
                .maxAmountInMinor(100)
                .frequency(Frequency.DAILY)
                .currency(CurrencyCode.GBP)
                .build();
        ApiResponse<SweepingSettings> updateSweepingResponse = tlClient.merchantAccounts()
                .updateSweeping(getMerchantAccount().getId(), updateSweepingRequest)
                .get();

        assertNotError(updateSweepingResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should disable sweeping for the given merchant account")
    public void itShouldDisableSweeping() {
        ApiResponse<Void> disableSweepingResponse = tlClient.merchantAccounts()
                .disableSweeping(getMerchantAccount().getId())
                .get();

        assertNotError(disableSweepingResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the payment sources for the given merchant account")
    public void itShouldGetPaymentSources() {
        ApiResponse<GetTransactionsResponse> getTransactionsResponse = getTransactions();
        assertNotError(getTransactionsResponse);
        MerchantAccountPayment merchantAccountPayment = getTransactionsResponse.getData().getItems().stream()
                .filter(t -> t.getType().equals(Transaction.Type.MERCHANT_ACCOUNT_PAYMENT))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("could not find a merchant account payment transaction"))
                .asMerchantAccountPayment();

        ;

        ApiResponse<GetPaymentSourcesResponse> getPaymentSources = tlClient.merchantAccounts()
                .getPaymentSources(
                        getMerchantAccount().getId(),
                        merchantAccountPayment.getPaymentSource().getUserId())
                .get();

        assertNotError(getPaymentSources);
    }

    @SneakyThrows
    @Synchronized
    private ApiResponse<GetTransactionsResponse> getTransactions() {
        return tlClient.merchantAccounts()
                .getTransactions(
                        getMerchantAccount().getId(), "2021-03-01T00:00:00.000Z", "2022-03-01T00:00:00.000Z", null)
                .get();
    }
}
