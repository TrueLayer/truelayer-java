package truelayer.java.acceptance;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static truelayer.java.TestUtils.assertNotError;

import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.*;
import truelayer.java.merchantaccounts.entities.sweeping.Frequency;
import truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;

@DisplayName("Merchant accounts acceptance tests")
public class MerchantAccountsAcceptanceTests extends AcceptanceTests {

    private MerchantAccount merchantAccount;

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
        ApiResponse<GetTransactionsResponse> getTransactionsResponse = tlClient.merchantAccounts()
                .getTransactions(
                        getMerchantAccount().getId(), "2021-03-01T00:00:00.000Z", "2022-03-01T00:00:00.000Z", null)
                .get();

        assertNotError(getTransactionsResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the sweeping settings for the given merchant account")
    public void itShouldGetTheSweepingSettings() {
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
        // todo manage this properly
        String aUserId = "4c5f09d8-fcb0-46f4-9f43-df58b158d980";

        ApiResponse<GetPaymentSourcesResponse> getPaymentSources = tlClient.merchantAccounts()
                .getPaymentSources(getMerchantAccount().getId(), aUserId)
                .get();

        assertNotError(getPaymentSources);
    }

    /**
     * Internal utility to save some API call
     */
    @SneakyThrows
    @Synchronized
    private MerchantAccount getMerchantAccount() {
        if (isNotEmpty(merchantAccount)) {
            return merchantAccount;
        }

        merchantAccount = tlClient.merchantAccounts().listMerchantAccounts().get().getData().getItems().stream()
                .filter(m -> m.getCurrency().equals(CurrencyCode.GBP))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("test merchant account not found"));

        return merchantAccount;
    }
}
