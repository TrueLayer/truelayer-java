package truelayer.java.acceptance;

import static truelayer.java.TestUtils.assertNotError;

import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.entities.CurrencyCode;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.GetTransactionsResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountsResponse;
import truelayer.java.merchantaccounts.entities.UpdateSweepingRequest;
import truelayer.java.merchantaccounts.entities.UpdateSweepingResponse;
import truelayer.java.merchantaccounts.entities.sweeping.Frequency;
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
        ApiResponse<ListMerchantAccountsResponse> merchantAccountsResponse =
                tlClient.merchantAccounts().listMerchantAccounts().get();
        String merchantAccountId =
                merchantAccountsResponse.getData().getItems().get(0).getId();
        ApiResponse<truelayer.java.merchantaccounts.entities.MerchantAccount> getMerchantAccountByIdResponse =
                tlClient.merchantAccounts()
                        .getMerchantAccountById(merchantAccountId)
                        .get();

        assertNotError(getMerchantAccountByIdResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of transactions for a given merchant account")
    public void itShouldGetTheListOfTransactions() {
        ApiResponse<ListMerchantAccountsResponse> merchantAccountsResponse =
                tlClient.merchantAccounts().listMerchantAccounts().get();
        String merchantAccountId =
                merchantAccountsResponse.getData().getItems().stream().filter(m->m.getCurrency().equals(CurrencyCode.GBP)).findFirst().get().getId();
        ApiResponse<GetTransactionsResponse> getTransactionsResponse = tlClient.merchantAccounts()
                .getTransactions(merchantAccountId, "2021-03-01T00:00:00.000Z", "2022-03-01T00:00:00.000Z", null)
                .get();

        assertNotError(getTransactionsResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should update the sweeping settings for the given merchant account")
    public void itShouldUpdateTheSweepingSettings() {
        ApiResponse<ListMerchantAccountsResponse> merchantAccountsResponse =
                tlClient.merchantAccounts().listMerchantAccounts().get();
        String merchantAccountId =
                merchantAccountsResponse.getData().getItems().stream().filter(m->m.getCurrency().equals(CurrencyCode.GBP)).findFirst().get().getId();

        UpdateSweepingRequest updateSweepingRequest = UpdateSweepingRequest.builder()
                .maxAmountInMinor(100)
                .frequency(Frequency.DAILY)
                .currency(CurrencyCode.GBP)
                .build();
        ApiResponse<UpdateSweepingResponse> updateSweepingResponse = tlClient.merchantAccounts()
                .updateSweeping(merchantAccountId, updateSweepingRequest).get();

        assertNotError(updateSweepingResponse);
    }
}
