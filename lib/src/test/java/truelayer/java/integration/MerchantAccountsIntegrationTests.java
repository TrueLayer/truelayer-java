package truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.merchantaccounts.entities.GetTransactionsResponse;
import truelayer.java.merchantaccounts.entities.ListMerchantAccountsResponse;
import truelayer.java.merchantaccounts.entities.MerchantAccount;
import truelayer.java.merchantaccounts.entities.transactions.TransactionTypeQuery;
import truelayer.java.payments.entities.*;

@DisplayName("Merchant accounts integration tests")
public class MerchantAccountsIntegrationTests extends IntegrationTests {
    public static final String A_MERCHANT_ACCOUNT_ID = "a-merchant-id";

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
        assertNotError(response);
        MerchantAccount expected = deserializeJsonFileTo(jsonResponseFile, MerchantAccount.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get the list of transactions for a given merchant account")
    public void shouldGetTransactions() {
        String jsonResponseFile = "payments/200.get_transactions.json";
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

        ApiResponse<GetTransactionsResponse> response = tlClient.merchantAccounts()
                .getTransactions(A_MERCHANT_ACCOUNT_ID, "2021-03-01", "2022-03-01", TransactionTypeQuery.PAYMENT)
                .get();

        assertNotError(response);
        GetTransactionsResponse expected = deserializeJsonFileTo(jsonResponseFile, GetTransactionsResponse.class);
        assertEquals(expected, response.getData());
    }
}
