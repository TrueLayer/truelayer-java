package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.CreatePaymentRequest;
import com.truelayer.java.paymentsproviders.entities.PaymentsProvider;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Payments providers integration tests")
public class PaymentsProvidersIntegrationTests extends IntegrationTests {

    @Test
    @DisplayName("It should create and return a payment")
    @SneakyThrows
    public void shouldCreateAndReturnAPaymentMerchantAccount() {
        String jsonResponseFile = "payments_providers/200.get_payments_provider.json";
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/payments-providers/ob-barclays"))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<PaymentsProvider> response = tlClient.paymentsProviders()
                .getProvider("ob-barclays", "client-id")
                .get();

        assertNotError(response);
        PaymentsProvider expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, PaymentsProvider.class);
        assertEquals(expected, response.getData());
    }
}
