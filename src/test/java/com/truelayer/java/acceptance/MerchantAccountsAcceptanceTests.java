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
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
                .getMerchantAccountById(getMerchantAccount(CurrencyCode.GBP).getId())
                .get();

        assertNotError(getMerchantAccountByIdResponse);
    }

    @SneakyThrows
    @DisplayName("It should get the list of transactions for a given merchant account")
    @ParameterizedTest(name = "with from={0} and to={1}")
    @MethodSource("provideFromAndToParameters")
    public void itShouldGetTheListOfTransactions(ZonedDateTime from, ZonedDateTime to) {
        ApiResponse<ListTransactionsResponse> transactionList = getTransactions(from, to);

        assertNotError(transactionList);
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
                .updateSweeping(getMerchantAccount(CurrencyCode.GBP).getId(), updateSweepingRequest)
                .get();
        assertNotError(updateSweepingResponse);

        ApiResponse<SweepingSettings> getSweepingSettingsResponse = tlClient.merchantAccounts()
                .getSweepingSettings(getMerchantAccount(CurrencyCode.GBP).getId())
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
                .updateSweeping(getMerchantAccount(CurrencyCode.GBP).getId(), updateSweepingRequest)
                .get();

        assertNotError(updateSweepingResponse);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should disable sweeping for the given merchant account")
    public void itShouldDisableSweeping() {
        ApiResponse<Void> disableSweepingResponse = tlClient.merchantAccounts()
                .disableSweeping(getMerchantAccount(CurrencyCode.GBP).getId())
                .get();

        assertNotError(disableSweepingResponse);
    }

    @SneakyThrows
    @DisplayName("It should get the payment sources for the given merchant account")
    @ParameterizedTest(name = "with from={0} and to={1}")
    @MethodSource("provideFromAndToParameters")
    public void itShouldGetPaymentSources(ZonedDateTime from, ZonedDateTime to) {
        ApiResponse<ListTransactionsResponse> getTransactionsResponse = getTransactions(from, to);
        assertNotError(getTransactionsResponse);
        MerchantAccountPayment merchantAccountPayment = getTransactionsResponse.getData().getItems().stream()
                .filter(t -> t.getType().equals(Transaction.Type.MERCHANT_ACCOUNT_PAYMENT))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("could not find a merchant account payment transaction"))
                .asMerchantAccountPayment();

        ApiResponse<ListPaymentSourcesResponse> getPaymentSources = tlClient.merchantAccounts()
                .listPaymentSources(
                        getMerchantAccount(CurrencyCode.GBP).getId(),
                        ListPaymentSourcesQuery.builder()
                                .userId(merchantAccountPayment
                                        .getPaymentSource()
                                        .getId())
                                .build())
                .get();

        assertNotError(getPaymentSources);
    }

    @SneakyThrows
    @Synchronized
    private ApiResponse<ListTransactionsResponse> getTransactions(ZonedDateTime from, ZonedDateTime to) {
        return tlClient.merchantAccounts()
                .listTransactions(
                        getMerchantAccount(CurrencyCode.GBP).getId(),
                        ListTransactionsQuery.builder().from(from).to(to).build())
                .get();
    }

    private static Stream<Arguments> provideFromAndToParameters() {
        return Stream.of(
                Arguments.of(
                        ZonedDateTime.of(LocalDate.of(2021, 3, 1), LocalTime.MIN, ZoneId.of("UTC")),
                        ZonedDateTime.of(LocalDate.of(2022, 3, 1), LocalTime.MIN, ZoneId.of("UTC"))),
                Arguments.of(
                        ZonedDateTime.of(LocalDate.of(2021, 3, 1), LocalTime.MIN, ZoneId.of("Europe/Paris")),
                        ZonedDateTime.of(LocalDate.of(2022, 3, 1), LocalTime.MIN, ZoneId.of("Europe/Paris"))),
                Arguments.of(ZonedDateTime.parse("2021-03-01T00:00:00Z"), ZonedDateTime.parse("2022-03-01T00:00:00Z")));
    }
}
