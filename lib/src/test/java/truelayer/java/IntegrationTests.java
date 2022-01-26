package truelayer.java;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.TestUtils.*;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.Collections;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import truelayer.java.auth.AuthenticationHandlerBuilder;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.configuration.Configuration;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.IPaymentHandler;
import truelayer.java.payments.PaymentHandler;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import truelayer.java.payments.entities.StartAuthorizationFlowResponse;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import truelayer.java.payments.entities.paymentdetail.Status;

@WireMockTest
@Tag("integration")
public class IntegrationTests {
    private static TrueLayerClient tlClient;

    public static final String A_PAYMENT_ID = "a-payment-id";

    @BeforeAll
    public static void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Configuration configuration = Configuration.builder()
                .versionInfo(Configuration.VersionInfo.builder()
                        .libraryName(LIBRARY_NAME)
                        .libraryVersion(LIBRARY_VERSION)
                        .build())
                .authentication(new Configuration.Endpoint(wireMockRuntimeInfo.getHttpBaseUrl()))
                .payments(Configuration.Payments.builder()
                        .endpointUrl(wireMockRuntimeInfo.getHttpBaseUrl())
                        .build())
                .hostedPaymentPage(new Configuration.Endpoint(wireMockRuntimeInfo.getHttpBaseUrl()))
                .build();

        IAuthenticationHandler authenticationHandler = AuthenticationHandlerBuilder.New()
                .configuration(configuration)
                .clientCredentials(getClientCredentials())
                .build();
        IPaymentHandler paymentsHandler = PaymentHandler.New()
                .configuration(configuration)
                .signingOptions(getSigningOptions())
                .authenticationHandler(authenticationHandler)
                .build();
        IHostedPaymentPageLinkBuilder hppBuilder = HostedPaymentPageLinkBuilder.New()
                .endpoint(configuration.hostedPaymentPage().endpointUrl())
                .build();

        tlClient = new TrueLayerClient(authenticationHandler, Optional.ofNullable(paymentsHandler), hppBuilder);
    }

    @Test
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    @SneakyThrows
    public void shouldReturnErrorIfUnauthorized() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(400)
                .bodyFile("auth/400.invalid_client.json")
                .build();

        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList("payments"))
                .get();

        assertTrue(response.isError());
        assertEquals("error", response.getError().getType());
        assertTrue(response.getError().getDetail().contains("invalid_client"));
    }

    @Test
    @DisplayName("It should create and return an access token")
    @SneakyThrows
    public void shouldReturnAnAccessToken() {
        String jsonResponseFile = "auth/200.access_token.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList("payments"))
                .get();

        assertNotError(response);
        AccessToken expected = deserializeJsonFileTo(jsonResponseFile, AccessToken.class);
        assertEquals(expected, response.getData());
    }

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
        CreatePaymentRequest paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

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
        String jsonResponseFile = new StringBuilder("payments/200.get_payment_by_id.")
                .append(expectedStatus.value())
                .append(".json")
                .toString();
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
    @ParameterizedTest(name = "with type {0}")
    @ValueSource(strings = {"provider_selection", "redirect", "wait"})
    @DisplayName("It should start an authorization flow")
    public void shouldStartAnAuthorizationFlow(String flowType) {
        String jsonResponseFile = "payments/200.start_authorization_flow." + flowType + ".json";
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
}
