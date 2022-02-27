package truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.paymentdetail.AuthorizationFlowAction;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import truelayer.java.payments.entities.paymentdetail.Status;

@DisplayName("Payments integration tests")
public class PaymentsIntegrationTests extends IntegrationTests {

    public static final String A_PAYMENT_ID = "a-payment-id";

    @Test
    @DisplayName("It should create and return a payment")
    @SneakyThrows
    public void shouldCreateAndReturnAPaymentMerchantAccount() {
        String jsonResponseFile = "payments/201.create_payment.merchant_account.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> response =
                tlClient.payments().createPayment(paymentRequest).get();

        assertNotError(response);
        CreatePaymentResponse expected = deserializeJsonFileTo(jsonResponseFile, CreatePaymentResponse.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should return an error if the signature is not valid")
    @SneakyThrows
    public void shouldReturnErrorIfSignatureIsInvalid() {
        String jsonResponseFile = "payments/401.invalid_signature.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(401)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertTrue(paymentResponse.isError());
        ProblemDetails expected = deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @DisplayName("it should get the payment details")
    @ParameterizedTest(name = "of a payment with status {0}")
    @ValueSource(strings = {"AUTHORIZATION_REQUIRED", "AUTHORIZING", "AUTHORIZED", "EXECUTED", "SETTLED", "FAILED"})
    @SneakyThrows
    public void shouldReturnAPaymentDetail(Status expectedStatus) {
        String jsonResponseFile = "payments/200.get_payment_by_id." + expectedStatus.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("get")
                .path(urlPathMatching("/payments/" + A_PAYMENT_ID))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<PaymentDetail> response =
                tlClient.payments().getPayment(A_PAYMENT_ID).get();

        assertNotError(response);
        PaymentDetail expected = deserializeJsonFileTo(jsonResponseFile, PaymentDetail.class);
        assertEquals(expectedStatus, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should return an error if a payment is not found")
    @SneakyThrows
    public void shouldThrowIfPaymentNotFound() {
        String jsonResponseFile = "payments/404.payment_not_found.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(404)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertTrue(paymentResponse.isError());
        ProblemDetails expected = deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @SneakyThrows
    @Test
    @DisplayName("It should return a request invalid error")
    public void shouldThrowARequestInvalidError() {
        String jsonResponseFile = "payments/400.request_invalid.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(400)
                .bodyFile(jsonResponseFile)
                .build();
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder().build();

        ApiResponse<CreatePaymentResponse> paymentResponse =
                tlClient.payments().createPayment(paymentRequest).get();

        assertTrue(paymentResponse.isError());
        ProblemDetails expected = deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(strings = {"PROVIDER_SELECTION", "REDIRECT", "WAIT"})
    @DisplayName("It should start an authorization flow")
    public void shouldStartAnAuthorizationFlow(AuthorizationFlowAction.Type flowType) {
        String jsonResponseFile = "payments/200.start_authorization_flow." + flowType.getType() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathMatching("/payments/" + A_PAYMENT_ID + "/authorization-flow"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();
        StartAuthorizationFlowRequest request =
                StartAuthorizationFlowRequest.builder().build();

        ApiResponse<StartAuthorizationFlowResponse> response = tlClient.payments()
                .startAuthorizationFlow(A_PAYMENT_ID, request)
                .get();

        assertNotError(response);
        StartAuthorizationFlowResponse expected =
                deserializeJsonFileTo(jsonResponseFile, StartAuthorizationFlowResponse.class);
        assertEquals(
                flowType,
                response.getData().getAuthorizationFlow().getActions().getNext().getType());
        assertEquals(expected, response.getData());
    }

    @SneakyThrows
    @ParameterizedTest(name = "and get a response of type {0}")
    @ValueSource(strings = {"AUTHORIZING", "FAILED"})
    @DisplayName("It should submit a provider selection")
    public void shouldSubmitProviderSelection(SubmitProviderSelectionResponse.Status status) {
        String jsonResponseFile = "payments/200.submit_provider_selection." + status.getStatus() + ".json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/payments/" + A_PAYMENT_ID + "/authorization-flow/actions/provider-selection"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        SubmitProviderSelectionRequest submitProviderSelectionRequest =
                SubmitProviderSelectionRequest.builder().build();
        ApiResponse<SubmitProviderSelectionResponse> response = tlClient.payments()
                .submitProviderSelection(A_PAYMENT_ID, submitProviderSelectionRequest)
                .get();

        assertNotError(response);
        SubmitProviderSelectionResponse expected =
                deserializeJsonFileTo(jsonResponseFile, SubmitProviderSelectionResponse.class);
        assertEquals(status, response.getData().getStatus());
        assertEquals(expected, response.getData());
    }
}
