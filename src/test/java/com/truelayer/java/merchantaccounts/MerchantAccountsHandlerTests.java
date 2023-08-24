package com.truelayer.java.merchantaccounts;

import static com.truelayer.java.TestUtils.buildTestHeaders;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.merchantaccounts.entities.ListPaymentSourcesQuery;
import com.truelayer.java.merchantaccounts.entities.ListTransactionsQuery;
import com.truelayer.java.merchantaccounts.entities.UpdateSweepingRequest;
import com.truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class MerchantAccountsHandlerTests {

    private static String A_MERCHANT_ACCOUNT_ID = "a-merchant-account-id";

    @Test
    @DisplayName("It should call the list merchant accounts endpoint")
    public void shouldCallListMerchantAccountsEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.listMerchantAccounts();

        verify(merchantsApi, times(1)).listMerchantAccounts();
    }

    @Test
    @DisplayName("It should call the get merchant account by id endpoint")
    public void shouldCallGetMerchantAccountByIdEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.getMerchantAccountById(A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).getMerchantAccountById(A_MERCHANT_ACCOUNT_ID);
    }

    @DisplayName("It should call the list transactions endpoint")
    @ParameterizedTest(name = "with from={0} and to={1}")
    @MethodSource("provideFromAndToParameters")
    public void shouldCallListTransactionsEndpoint(ZonedDateTime from, ZonedDateTime to) {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        ListTransactionsQuery query = ListTransactionsQuery.builder()
                .from(from)
                .to(to)
                .type(TransactionTypeQuery.PAYOUT)
                .build();

        sut.listTransactions(A_MERCHANT_ACCOUNT_ID, query);

        verify(merchantsApi, times(1))
                .listTransactions(
                        A_MERCHANT_ACCOUNT_ID,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(from),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(to),
                        query.type());
    }

    @Test
    @DisplayName("It should call the update sweeping endpoint with empty headers map")
    public void shouldCallUpdateSweepingEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        UpdateSweepingRequest request =
                UpdateSweepingRequest.builder().currency(CurrencyCode.GBP).build();

        sut.updateSweeping(A_MERCHANT_ACCOUNT_ID, request);

        verify(merchantsApi, times(1)).updateSweeping(emptyMap(), A_MERCHANT_ACCOUNT_ID, request);
    }

    @Test
    @DisplayName("It should call the update sweeping endpoint with custom headers")
    public void shouldCallUpdateSweepingEndpointWithCustomHeaders() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        Headers customHeaders = buildTestHeaders();
        UpdateSweepingRequest request =
                UpdateSweepingRequest.builder().currency(CurrencyCode.GBP).build();

        sut.updateSweeping(customHeaders, A_MERCHANT_ACCOUNT_ID, request);

        verify(merchantsApi, times(1)).updateSweeping(toMap(customHeaders), A_MERCHANT_ACCOUNT_ID, request);
    }

    @Test
    @DisplayName("It should call the get sweeping settings endpoint")
    public void shouldCallGetSweepingSettingsEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.getSweepingSettings(A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).getSweepingSettings(A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint with empty headers map")
    public void shouldCallDisableSweepingEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);

        sut.disableSweeping(A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).disableSweeping(emptyMap(), A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint")
    public void shouldCallDisableSweepingEndpointWithCustomHeaders() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        Headers customHeaders = buildTestHeaders();

        sut.disableSweeping(customHeaders, A_MERCHANT_ACCOUNT_ID);

        verify(merchantsApi, times(1)).disableSweeping(toMap(customHeaders), A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint")
    public void shouldCallListPaymentSourcesEndpoint() {
        IMerchantAccountsApi merchantsApi = Mockito.mock(IMerchantAccountsApi.class);
        MerchantAccountsHandler sut = new MerchantAccountsHandler(merchantsApi);
        ListPaymentSourcesQuery query =
                ListPaymentSourcesQuery.builder().userId("a-user-id").build();

        sut.listPaymentSources(A_MERCHANT_ACCOUNT_ID, query);

        verify(merchantsApi, times(1)).listPaymentSources(A_MERCHANT_ACCOUNT_ID, query.userId());
    }

    private static Stream<Arguments> provideFromAndToParameters() {
        return Stream.of(
                Arguments.of(ZonedDateTime.now(), ZonedDateTime.now().plusMonths(-12)),
                Arguments.of(
                        ZonedDateTime.now(ZoneId.of("Europe/Paris")),
                        ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusMonths(-12)),
                Arguments.of(
                        ZonedDateTime.of(LocalDate.of(2021, 3, 1), LocalTime.MIN, ZoneId.of("UTC")),
                        ZonedDateTime.of(LocalDate.of(2022, 3, 1), LocalTime.MIN, ZoneId.of("UTC"))),
                Arguments.of(ZonedDateTime.parse("2021-03-01T00:00:00Z"), ZonedDateTime.parse("2022-03-01T00:00:00Z")));
    }
}
