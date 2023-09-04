package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
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
        String providerId = "ob-barclays";
        String clientId = TestUtils.getClientCredentials().clientId();
        RequestStub.New()
                .method("get")
                .path(urlEqualTo("/payments-providers/" + providerId + "?client_id=" + clientId))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<PaymentsProvider> response =
                tlClient.paymentsProviders().getProvider(providerId).get();

        verify(exactly(0), postRequestedFor(urlPathEqualTo("/connect/token")));
        assertNotError(response);
        PaymentsProvider expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, PaymentsProvider.class);
        assertEquals(expected, response.getData());
    }
}
