package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.Scopes.RECURRING_PAYMENTS_SWEEPING;
import static com.truelayer.java.TestUtils.assertNotError;
import static com.truelayer.java.TestUtils.deserializeJsonFileTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.entities.RelatedProducts;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.mandates.entities.*;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.mandates.entities.mandatedetail.Status;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import java.util.Collections;
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
                .withIdempotencyKey()
                .status(201)
                .bodyFile(jsonResponseFile)
                .build();
        CreateMandateRequest createMandateRequest =
                CreateMandateRequest.builder().build();

        ApiResponse<CreateMandateResponse> response =
                tlClient.mandates().createMandate(createMandateRequest).get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        assertNotError(response);
        CreateMandateResponse expected = deserializeJsonFileTo(jsonResponseFile, CreateMandateResponse.class);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create a mandate with additional product intention")
    public void shouldCreateAMandateWithAdditionalProductIntention() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        CreateMandateRequest createMandateRequest = CreateMandateRequest.builder()
                .relatedProducts(RelatedProducts.builder()
                        .signupPlus(Collections.singletonMap("foo", "bar"))
                        .build())
                .build();

        tlClient.mandates().createMandate(createMandateRequest).get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        verify(postRequestedFor(urlPathEqualTo("/mandates"))
                .withRequestBody(
                        matchingJsonPath("$.related_products", equalToJson("{\"signup_plus\": {\"foo\": \"bar\"}}"))));
    }

    @Test
    @DisplayName("It should get a list of mandates")
    @SneakyThrows
    public void shouldGetAListOfMandates() {
        String jsonResponseFile = "mandates/200.list_mandates.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/mandates"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<ListMandatesResponse> response =
                tlClient.mandates().listMandates().get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        ListMandatesResponse expected = deserializeJsonFileTo(jsonResponseFile, ListMandatesResponse.class);
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

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        MandateDetail expected = deserializeJsonFileTo(jsonResponseFile, MandateDetail.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(strings = {"provider_selection", "redirect", "wait"})
    @DisplayName("It should start an authorization flow")
    public void shouldStartAnAuthorizationFlow(String action) {
        String jsonResponseFile = "mandates/200.start_authorization_flow.authorizing." + action + ".json";
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
                .withIdempotencyKey()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        ApiResponse<AuthorizationFlowResponse> response = tlClient.mandates()
                .startAuthorizationFlow(A_MANDATE_ID, request)
                .get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        assertNotError(response);
        AuthorizationFlowResponse expected = deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
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
                .withIdempotencyKey()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().build();
        ApiResponse<AuthorizationFlowResponse> response = tlClient.mandates()
                .submitProviderSelection(A_MANDATE_ID, submitProviderSelectionRequest)
                .get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        assertNotError(response);
        AuthorizationFlowResponse expected = deserializeJsonFileTo(jsonResponseFile, AuthorizationFlowResponse.class);
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
                .withIdempotencyKey()
                .status(204)
                .build();

        ApiResponse<Void> response =
                tlClient.mandates().revokeMandate(A_MANDATE_ID).get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        assertNotError(response);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get funds")
    public void shouldGetFundsConfirmation() {
        String jsonResponseFile = "mandates/200.get_confirmation_of_funds.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/mandates/" + A_MANDATE_ID + "/funds"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<GetConfirmationOfFundsResponse> response = tlClient.mandates()
                .getConfirmationOfFunds(A_MANDATE_ID, "1", "gbp")
                .get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        GetConfirmationOfFundsResponse expected =
                deserializeJsonFileTo(jsonResponseFile, GetConfirmationOfFundsResponse.class);
        assertNotError(response);
        assertEquals(expected.getConfirmed(), response.getData().getConfirmed());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should get constraints")
    public void shouldGetConstraints() {
        String jsonResponseFile = "mandates/200.get_constraints.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        RequestStub.New()
                .method("get")
                .path(urlPathEqualTo("/mandates/" + A_MANDATE_ID + "/constraints"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<GetConstraintsResponse> response =
                tlClient.mandates().getMandateConstraints(A_MANDATE_ID).get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        GetConstraintsResponse expected = deserializeJsonFileTo(jsonResponseFile, GetConstraintsResponse.class);
        assertNotError(response);
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should yield an idempotency key reuse error")
    public void shouldYieldAnIdempotencyKeyReuseError() {
        String jsonResponseFile = "mandates/422.create_mandate.idempotency_key_reuse.json";
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
                .withIdempotencyKey()
                .status(422)
                .bodyFile(jsonResponseFile)
                .build();

        CreateMandateRequest createMandateRequest =
                CreateMandateRequest.builder().build();

        ApiResponse<CreateMandateResponse> response =
                tlClient.mandates().createMandate(createMandateRequest).get();

        verifyGeneratedToken(Collections.singletonList(RECURRING_PAYMENTS_SWEEPING));
        assertTrue(response.isError());
        assertEquals(response.getError(), deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class));
    }
}
