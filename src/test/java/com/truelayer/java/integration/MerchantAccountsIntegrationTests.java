package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
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
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts"))
                .withAuthorization()
                .status(200)
                .withResponseBodyFile(jsonResponseFile)
                .build();

        ApiResponse<ListMerchantAccountsResponse> response =
                tlClient.merchantAccounts().listMerchantAccounts().get();
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
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID))
                .withAuthorization()
                .status(200)
                .withResponseBodyFile(jsonResponseFile)
                .build();

        ApiResponse<MerchantAccount> response = tlClient.merchantAccounts()
                .getMerchantAccountById(A_MERCHANT_ACCOUNT_ID)
                .get();
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
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/transactions"))
                .withAuthorization()
                .status(200)
                .withResponseBodyFile(jsonResponseFile)
                .build();

        ApiResponse<GetTransactionsResponse> response = tlClient.merchantAccounts()
                .getTransactions(A_MERCHANT_ACCOUNT_ID, "2021-03-01", "2022-03-01", TransactionTypeQuery.PAYMENT)
                .get();

        assertNotError(response);
        GetTransactionsResponse expected = deserializeJsonFileTo(jsonResponseFile, GetTransactionsResponse.class);
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
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/sweeping"))
                .withAuthorization()
                .status(200)
                .withResponseBodyFile(jsonResponseFile)
                .build();

        ApiResponse<SweepingSettings> response = tlClient.merchantAccounts()
                .getSweepingSettings(A_MERCHANT_ACCOUNT_ID)
                .get();

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
                .withResponseBodyFile("auth/200.access_token.json")
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
                .withResponseBodyFile("auth/200.access_token.json")
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
                .withResponseBodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/merchant-accounts/" + A_MERCHANT_ACCOUNT_ID + "/payment-sources"))
                .withAuthorization()
                .status(200)
                .withResponseBodyFile(jsonResponseFile)
                .build();

        ApiResponse<GetPaymentSourcesResponse> response = tlClient.merchantAccounts()
                .getPaymentSources(A_MERCHANT_ACCOUNT_ID, A_USER_ID)
                .get();

        assertNotError(response);
        GetPaymentSourcesResponse expected = deserializeJsonFileTo(jsonResponseFile, GetPaymentSourcesResponse.class);
        assertEquals(expected, response.getData());
    }
}
