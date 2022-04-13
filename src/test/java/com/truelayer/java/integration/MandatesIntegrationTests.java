package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.mandates.entities.mandatedetail.Status;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Mandates integration tests")
public class MandatesIntegrationTests extends IntegrationTests {

    public static final String A_MANDATE_ID = "a-mandate-id";

    @SneakyThrows
    @Test
    @DisplayName("It should create a mandate")
    public void shouldCreateAMandate() {
        String jsonResponseFile = "mandates/201.create_mandate.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/mandates"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .bodyFile(jsonResponseFile)
                .build();
        CreateMandateRequest createMandateRequest =
                CreateMandateRequest.builder().build();

        ApiResponse<CreateMandateResponse> response =
                tlClient.mandates().createMandate(createMandateRequest).get();

        assertNotError(response);
        CreateMandateResponse expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, CreateMandateResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @DisplayName("It should get the mandate details")
    @ParameterizedTest(name = "of a mandate with status {0}")
    @ValueSource(strings = {"AUTHORIZATION_REQUIRED", "AUTHORIZING", "AUTHORIZED", "REVOKED", "FAILED"})
    public void shouldGetAMandateById(Status expectedStatus) {
        String jsonResponseFile = "mandates/200.get_mandate_by_id." + expectedStatus.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathMatching("/mandates/" + A_MANDATE_ID))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<MandateDetail> response =
                tlClient.mandates().getMandate(A_MANDATE_ID).get();
        MandateDetail expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, MandateDetail.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(strings = {"provider_selection", "redirect", "wait"})
    @DisplayName("It should start an authorization flow")
    public void shouldStartAnAuthorizationFlow(String status) {
        String jsonResponseFile = "mandates/200.start_authorization_flow.authorizing." + status + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathMatching("/mandates/" + A_MANDATE_ID + "/authorization-flow"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        ApiResponse<AuthorizationFlowResponse> response = tlClient.mandates()
                .startAuthorizationFlow(A_MANDATE_ID, request)
                .get();

        assertNotError(response);
        AuthorizationFlowResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(strings = {"AUTHORIZING", "FAILED"})
    @DisplayName("It should submit a provider selection")
    public void shouldSubmitProviderSelection(com.truelayer.java.payments.entities.paymentdetail.Status status) {
        String jsonResponseFile = "mandates/200.submit_provider_selection." + status.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/mandates/" + A_MANDATE_ID + "/authorization-flow/actions/provider-selection"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().build();
        ApiResponse<AuthorizationFlowResponse> response = tlClient.mandates()
                .submitProviderSelection(A_MANDATE_ID, submitProviderSelectionRequest)
                .get();

        assertNotError(response);
        AuthorizationFlowResponse expected =
                TestUtils.deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
        assertEquals(status, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should revoke a mandate")
    public void shouldRevokeAMandate() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/mandates/" + A_MANDATE_ID + "/revoke"))
                .withAuthorization()
                .status(204)
                .build();

        ApiResponse<Void> response =
                tlClient.mandates().revokeMandate(A_MANDATE_ID).get();

        assertNotError(response);
    }
}
