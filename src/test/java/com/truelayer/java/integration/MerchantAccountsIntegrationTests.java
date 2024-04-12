package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.merchantaccounts.entities.*;
import com.truelayer.java.merchantaccounts.entities.sweeping.Frequency;
import com.truelayer.java.merchantaccounts.entities.sweeping.SweepingSettings;
import com.truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Merchant accounts integration tests")
public class MerchantAccountsIntegrationTests extends IntegrationTests {
    public static final String A_MERCHANT_ACCOUNT_ID = "a-merchant-id";
    public static final String A_USER_ID = "a-user-id";

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of all merchant accounts associated to the given client")
    public void itShouldListAllMerchantAccounts() {
        String jsonResponseFile = "merchant_accounts/200.list_merchant_accounts.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<ListMerchantAccountsResponse> response =
                tlClient.merchantAccounts().listMerchantAccounts().get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        ListMerchantAccountsResponse expected =
                deserializeJsonFileTo(jsonResponseFile, ListMerchantAccountsResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get a merchant account by id")
    public void itShouldGetAMerchantAccountById() {
        String jsonResponseFile = "merchant_accounts/200.get_merchant_account_by_id.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<MerchantAccount> response = tlClient.merchantAccounts()
                .getMerchantAccountById(A_MERCHANT_ACCOUNT_ID)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        MerchantAccount expected = deserializeJsonFileTo(jsonResponseFile, MerchantAccount.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of transactions for a given merchant account")
    public void shouldGetTransactions() {
        String jsonResponseFile = "merchant_accounts/200.get_transactions.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/transactions"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        String from = "2022-01-01T00:00:00Z";
        String to = "2022-03-01T00:00:00Z";
        ListTransactionsQuery query = ListTransactionsQuery.builder()
                .from(ZonedDateTime.parse(from))
                .to(ZonedDateTime.parse(to))
                .type(TransactionTypeQuery.PAYMENT)
                .build();
        ApiResponse<ListTransactionsResponse> response = tlClient.merchantAccounts()
                .listTransactions(A_MERCHANT_ACCOUNT_ID, query)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        verify(getRequestedFor(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/transactions"))
                .withQueryParam("from", equalTo(to))
                .withQueryParam("from", equalTo(from))
                .withQueryParam("type", equalTo(TransactionTypeQuery.PAYMENT.toString()))
                .withoutQueryParam("cursor"));

        assertNotError(response);
        ListTransactionsResponse expected = deserializeJsonFileTo(jsonResponseFile, ListTransactionsResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of transactions for a given merchant account with pagination cursor")
    public void shouldGetTransactionsWithPaginationCursor() {
        String jsonResponseFile = "merchant_accounts/200.get_transactions.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/transactions"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        String from = "2022-01-01T00:00:00Z";
        String to = "2022-03-01T00:00:00Z";
        String cursor = UUID.randomUUID().toString();
        ListTransactionsQuery query = ListTransactionsQuery.builder()
                .from(ZonedDateTime.parse(from))
                .to(ZonedDateTime.parse(to))
                .type(TransactionTypeQuery.PAYMENT)
                .cursor(cursor)
                .build();
        ApiResponse<ListTransactionsResponse> response = tlClient.merchantAccounts()
                .listTransactions(A_MERCHANT_ACCOUNT_ID, query)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        verify(getRequestedFor(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/transactions"))
                .withQueryParam("from", equalTo(to))
                .withQueryParam("from", equalTo(from))
                .withQueryParam("type", equalTo(TransactionTypeQuery.PAYMENT.toString()))
                .withQueryParam("cursor", equalTo(cursor)));

        assertNotError(response);
        ListTransactionsResponse expected = deserializeJsonFileTo(jsonResponseFile, ListTransactionsResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the sweeping settings for a given merchant account")
    public void shouldGetTheSweepingSettings() {
        String jsonResponseFile = "merchant_accounts/200.sweeping_settings.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/sweeping"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<SweepingSettings> response = tlClient.merchantAccounts()
                .getSweepingSettings(A_MERCHANT_ACCOUNT_ID)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        SweepingSettings expected = deserializeJsonFileTo(jsonResponseFile, SweepingSettings.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should update the sweeping setup for a given merchant account")
    public void shouldUpdateSweepingSetup() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/sweeping"))
                .withAuthorization()
                .status(204)
                .build();

        UpdateSweepingRequest updateSweepingRequest = UpdateSweepingRequest.builder()
                .currency(CurrencyCode.EUR)
                .frequency(Frequency.DAILY)
                .maxAmountInMinor(100)
                .build();
        ApiResponse<SweepingSettings> response = tlClient.merchantAccounts()
                .updateSweeping(A_MERCHANT_ACCOUNT_ID, updateSweepingRequest)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should disable sweeping for a given merchant account")
    public void shouldDisableSweeping() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("delete")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/sweeping"))
                .withAuthorization()
                .status(204)
                .build();

        ApiResponse<Void> response = tlClient.merchantAccounts()
                .disableSweeping(A_MERCHANT_ACCOUNT_ID)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the payment sources for a given merchant account")
    public void shouldGetPaymentSources() {
        String jsonResponseFile = "merchant_accounts/200.payment_sources.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/payment-sources"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ListPaymentSourcesQuery query =
                ListPaymentSourcesQuery.builder().userId(A_USER_ID).build();
        ApiResponse<ListPaymentSourcesResponse> response = tlClient.merchantAccounts()
                .listPaymentSources(A_MERCHANT_ACCOUNT_ID, query)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        ListPaymentSourcesResponse expected = deserializeJsonFileTo(jsonResponseFile, ListPaymentSourcesResponse.class);
        assertEquals(expected, response.getData());
    }
}
