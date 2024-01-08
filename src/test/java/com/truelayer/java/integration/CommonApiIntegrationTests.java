package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnResponse;
import com.truelayer.java.http.entities.ApiResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("General endpoints integration tests")
public class CommonApiIntegrationTests extends IntegrationTests {

    @Test
    @DisplayName("It should return a payment resource if provider return params are submitted")
    @SneakyThrows
    public void shouldReturnAResourceWhenProviderReturnParamsSubmitted() {
        String jsonResponseFile = "common/200.submit_payment_returns_parameters.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments-provider-return"))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<SubmitPaymentsProviderReturnResponse> submitPaymentReturnParametersResponse =
                tlClient.submitPaymentReturnParameters(
                                SubmitPaymentsProviderReturnRequest.builder().build())
                        .get();

        verify(exactly(0), postRequestedFor(urlPathEqualTo("/connect/token")));
        assertNotError(submitPaymentReturnParametersResponse);
        SubmitPaymentsProviderReturnResponse expected =
                deserializeJsonFileTo(jsonResponseFile, SubmitPaymentsProviderReturnResponse.class);
        assertEquals(expected, submitPaymentReturnParametersResponse.getData());
    }
}
