package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.CountryCode;
import com.truelayer.java.payments.entities.CustomerSegment;
import com.truelayer.java.payments.entities.ReleaseChannel;
import com.truelayer.java.paymentsproviders.entities.*;
import com.truelayer.java.paymentsproviders.entities.searchproviders.*;
import com.truelayer.java.paymentsproviders.entities.searchproviders.BankTransferCapabilities;
import com.truelayer.java.paymentsproviders.entities.searchproviders.Capabilities;
import com.truelayer.java.paymentsproviders.entities.searchproviders.PaymentsCapabilities;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Payments providers integration tests")
public class PaymentsProvidersIntegrationTests extends IntegrationTests {

    @Test
    @DisplayName("It should return a payments provider")
    @SneakyThrows
    public void shouldReturnAPaymentsProvider() {
        String jsonResponseFile = "payments_providers/200.get_payments_provider.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        String providerId = "ob-barclays";
        RequestStub.New()
                .method("get")
                .path(urlEqualTo("/payments-providers/" + providerId))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<PaymentsProvider> response =
                tlClient.paymentsProviders().getProvider(providerId).get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        PaymentsProvider expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, PaymentsProvider.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should return a list of payments provider after search")
    @SneakyThrows
    public void shouldReturnAListOfPaymentsProvidersOnSearchPaymentsProviders() {
        String jsonResponseFile = "payments_providers/200.search_payments_providers.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlEqualTo("/payments-providers/search"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        SearchPaymentProvidersRequest searchPaymentProvidersRequest = SearchPaymentProvidersRequest.builder()
                .countries(List.of(CountryCode.GB))
                .currencies(List.of(CurrencyCode.GBP))
                .releaseChannel(ReleaseChannel.PRIVATE_BETA)
                .capabilities(Capabilities.builder()
                        .payments(PaymentsCapabilities.builder()
                                .bankTransfer(new BankTransferCapabilities())
                                .build())
                        .build())
                .customerSegments(List.of(CustomerSegment.RETAIL))
                .authorizationFlow(AuthorizationFlow.builder()
                        .configuration(Configuration.builder()
                                .redirect(Redirect.builder().build())
                                .build())
                        .build())
                .build();

        ApiResponse<SearchPaymentProvidersResponse> response = tlClient.paymentsProviders()
                .searchProviders(searchPaymentProvidersRequest)
                .get();

        verifyGeneratedToken(Collections.singletonList(PAYMENTS));
        assertNotError(response);
        SearchPaymentProvidersResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, SearchPaymentProvidersResponse.class);
        assertEquals(expected, response.getData());
    }
}
