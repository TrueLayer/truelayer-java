package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.http.entities.ApiResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

        ApiResponse<SubmitPaymentReturnParametersResponse> submitPaymentReturnParametersResponse =
                tlClient.submitPaymentReturnParameters(
                                SubmitPaymentReturnParametersRequest.builder().build())
                        .get();

        assertNotError(submitPaymentReturnParametersResponse);
        SubmitPaymentReturnParametersResponse expected =
                deserializeJsonFileTo(jsonResponseFile, SubmitPaymentReturnParametersResponse.class);
        assertEquals(expected, submitPaymentReturnParametersResponse.getData());
    }
}
