package com.truelayer.java.merchantaccounts;

import static com.truelayer.java.TestUtils.buildTestHeaders;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.truelayer.java.Constants;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.entities.RequestScopes;
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
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class MerchantAccountsHandlerTests {

    private static final String A_MERCHANT_ACCOUNT_ID = "a-merchant-account-id";

    private static final RequestScopes SCOPES = RequestScopes.builder()
            .scope(Constants.Scopes.PAYMENTS)
            .scope("a-custom-scope")
            .build();

    private MerchantAccountsHandler sut;
    private IMerchantAccountsApi merchantAccountsApiMock;

    @BeforeEach
    public void setup() {
        merchantAccountsApiMock = Mockito.mock(IMerchantAccountsApi.class);
        sut = MerchantAccountsHandler.builder()
                .merchantAccountsApi(merchantAccountsApiMock)
                .scopes(SCOPES)
                .build();
    }

    @Test
    @DisplayName("It should call the list merchant accounts endpoint with the default scopes")
    public void shouldCallListMerchantAccountsWithDefaultScopes() {
        MerchantAccountsHandler sut = MerchantAccountsHandler.builder()
                .merchantAccountsApi(merchantAccountsApiMock)
                .build();

        sut.listMerchantAccounts();

        RequestScopes expectedDefaultScopes =
                RequestScopes.builder().scope(Constants.Scopes.PAYMENTS).build();
        verify(merchantAccountsApiMock, times(1)).listMerchantAccounts(expectedDefaultScopes);
    }

    @Test
    @DisplayName("It should call the list merchant accounts endpoint")
    public void shouldCallListMerchantAccountsEndpoint() {
        sut.listMerchantAccounts();

        verify(merchantAccountsApiMock, times(1)).listMerchantAccounts(SCOPES);
    }

    @Test
    @DisplayName("It should call the get merchant account by id endpoint")
    public void shouldCallGetMerchantAccountByIdEndpoint() {
        sut.getMerchantAccountById(A_MERCHANT_ACCOUNT_ID);

        verify(merchantAccountsApiMock, times(1)).getMerchantAccountById(SCOPES, A_MERCHANT_ACCOUNT_ID);
    }

    @DisplayName("It should call the list transactions endpoint")
    @ParameterizedTest(name = "with from={0}, to={1} and cursor={2}")
    @MethodSource("provideFromAndToParameters")
    public void shouldCallListTransactionsEndpoint(ZonedDateTime from, ZonedDateTime to, String cursor) {
        ListTransactionsQuery query = ListTransactionsQuery.builder()
                .from(from)
                .to(to)
                .type(TransactionTypeQuery.PAYOUT)
                .cursor(cursor)
                .build();

        sut.listTransactions(A_MERCHANT_ACCOUNT_ID, query);

        verify(merchantAccountsApiMock, times(1))
                .listTransactions(
                        SCOPES,
                        emptyMap(),
                        A_MERCHANT_ACCOUNT_ID,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(from),
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(to),
                        query.type(),
                        query.cursor());
    }

    @Test
    @DisplayName("It should call the update sweeping endpoint with empty headers map")
    public void shouldCallUpdateSweepingEndpoint() {
        UpdateSweepingRequest request =
                UpdateSweepingRequest.builder().currency(CurrencyCode.GBP).build();

        sut.updateSweeping(A_MERCHANT_ACCOUNT_ID, request);

        verify(merchantAccountsApiMock, times(1)).updateSweeping(SCOPES, emptyMap(), A_MERCHANT_ACCOUNT_ID, request);
    }

    @Test
    @DisplayName("It should call the update sweeping endpoint with custom headers")
    public void shouldCallUpdateSweepingEndpointWithCustomHeaders() {
        Headers customHeaders = buildTestHeaders();
        UpdateSweepingRequest request =
                UpdateSweepingRequest.builder().currency(CurrencyCode.GBP).build();

        sut.updateSweeping(customHeaders, A_MERCHANT_ACCOUNT_ID, request);

        verify(merchantAccountsApiMock, times(1))
                .updateSweeping(SCOPES, toMap(customHeaders), A_MERCHANT_ACCOUNT_ID, request);
    }

    @Test
    @DisplayName("It should call the get sweeping settings endpoint")
    public void shouldCallGetSweepingSettingsEndpoint() {
        sut.getSweepingSettings(A_MERCHANT_ACCOUNT_ID);

        verify(merchantAccountsApiMock, times(1)).getSweepingSettings(SCOPES, A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint with empty headers map")
    public void shouldCallDisableSweepingEndpoint() {
        sut.disableSweeping(A_MERCHANT_ACCOUNT_ID);

        verify(merchantAccountsApiMock, times(1)).disableSweeping(SCOPES, emptyMap(), A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint")
    public void shouldCallDisableSweepingEndpointWithCustomHeaders() {
        Headers customHeaders = buildTestHeaders();

        sut.disableSweeping(customHeaders, A_MERCHANT_ACCOUNT_ID);

        verify(merchantAccountsApiMock, times(1)).disableSweeping(SCOPES, toMap(customHeaders), A_MERCHANT_ACCOUNT_ID);
    }

    @Test
    @DisplayName("It should call the disable sweeping endpoint")
    public void shouldCallListPaymentSourcesEndpoint() {
        ListPaymentSourcesQuery query =
                ListPaymentSourcesQuery.builder().userId("a-user-id").build();

        sut.listPaymentSources(A_MERCHANT_ACCOUNT_ID, query);

        verify(merchantAccountsApiMock, times(1)).listPaymentSources(SCOPES, A_MERCHANT_ACCOUNT_ID, query.userId());
    }

    private static Stream<Arguments> provideFromAndToParameters() {
        return Stream.of(
                Arguments.of(ZonedDateTime.now(), ZonedDateTime.now().plusMonths(-12), "a-cursor"),
                Arguments.of(
                        ZonedDateTime.now(ZoneId.of("Europe/Paris")),
                        ZonedDateTime.now(ZoneId.of("Europe/Paris")).plusMonths(-12),
                        UUID.randomUUID().toString()),
                Arguments.of(
                        ZonedDateTime.of(LocalDate.of(2021, 3, 1), LocalTime.MIN, ZoneId.of("UTC")),
                        ZonedDateTime.of(LocalDate.of(2022, 3, 1), LocalTime.MIN, ZoneId.of("UTC")),
                        "another-cursor"),
                Arguments.of(
                        ZonedDateTime.parse("2021-03-01T00:00:00Z"),
                        ZonedDateTime.parse("2022-03-01T00:00:00Z"),
                        "yet-another-cursor"));
    }
}
