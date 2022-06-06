package com.truelayer.java.acceptance;

import static com.truelayer.java.TestUtils.assertNotError;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.Frequency;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import com.truelayer.java.merchantaccounts.entities.transactions.MerchantAccountPayment;
import com.truelayer.java.merchantaccounts.entities.transactions.Transaction;
import java.time.*;
import java.time.temporal.ChronoUnit;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        ApiResponse<MerchantAccount> getMerchantAccountByIdResponse = tlClient.merchantAccounts()
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
        ApiResponse<ListTransactionsResponse> getTransactionsResponse = getTransactions();
        assertNotError(getTransactionsResponse);
        MerchantAccountPayment merchantAccountPayment = getTransactionsResponse.getData().getItems().stream()
                .filter(t -> t.getType().equals(Transaction.Type.MERCHANT_ACCOUNT_PAYMENT))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("could not find a merchant account payment transaction"))
                .asMerchantAccountPayment();

        ApiResponse<ListPaymentSourcesResponse> getPaymentSources = tlClient.merchantAccounts()
                .listPaymentSources(
                        getMerchantAccount().getId(),
                        ListPaymentSourcesQuery.builder()
                                .userId(merchantAccountPayment
                                        .getPaymentSource()
                                        .getUserId())
                                .build())
                .get();

        assertNotError(getPaymentSources);
    }

    @SneakyThrows
    @Synchronized
    private ApiResponse<ListTransactionsResponse> getTransactions() {
        ZonedDateTime from = ZonedDateTime.parse("2021-03-01T00:00:00Z");
        return tlClient.merchantAccounts()
                .listTransactions(
                        getMerchantAccount().getId(),
                        ListTransactionsQuery.builder()
                                .from(from)
                                .to(from.plus(1, ChronoUnit.YEARS))
                                .build())
                .get();
    }
}
