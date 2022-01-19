package truelayer.java;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.TestUtils.*;
import static truelayer.java.common.Constants.ConfigurationKeys.*;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import truelayer.java.TestUtils.*;
import truelayer.java.auth.AuthenticationHandlerBuilder;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.configuration.Configuration;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.PaymentHandler;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;

@WireMockTest
@Tag("integration")
public class IntegrationTests {
    private static TrueLayerClient tlClient;

    @BeforeAll
    public static void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        var configuration = Configuration.builder()
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

        var authenticationHandler = AuthenticationHandlerBuilder.New()
                .configuration(configuration)
                .clientCredentials(getClientCredentials())
                .build();
        var paymentsHandler = PaymentHandler.New()
                .configuration(configuration)
                .signingOptions(getSigningOptions())
                .authenticationHandler(authenticationHandler)
                .build();
        var hppBuilder = HostedPaymentPageLinkBuilder.New()
                .endpoint(configuration.hostedPaymentPage().endpointUrl())
                .build();

        tlClient = new TrueLayerClient(authenticationHandler, Optional.ofNullable(paymentsHandler), hppBuilder);
    }

    @Test
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    @SneakyThrows
    public void shouldReturnErrorIfUnauthorized() {
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(400)
                .bodyFile("auth/400.invalid_client.json")
                .build();

        var response = tlClient.auth().getOauthToken(List.of("payments")).get();

        assertTrue(response.isError());
        assertEquals("error", response.getError().getType());
        assertTrue(response.getError().getDetail().contains("invalid_client"));
    }

    @Test
    @DisplayName("It should create and return an access token")
    @SneakyThrows
    public void shouldReturnAnAccessToken() {
        var jsonResponseFile = "auth/200.access_token.json";
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        var response = tlClient.auth().getOauthToken(List.of("payments")).get();

        assertNotError(response);
        var expected = deserializeJsonFileTo(jsonResponseFile, AccessToken.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should create and return a payment")
    public void shouldCreateAndReturnAPaymentMerchantAccount() {
        var jsonResponseFile = "payments/201.create_payment.merchant_account.json";
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(201)
                .bodyFile(jsonResponseFile)
                .build();
        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

        var response = tlClient.payments().createPayment(paymentRequest);

        assertNotError(response);
        var expected = deserializeJsonFileTo(jsonResponseFile, CreatePaymentResponse.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should return an error if the signature is not valid")
    public void shouldReturnErrorIfSignatureIsInvalid() {
        var jsonResponseFile = "payments/401.invalid_signature.json";
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(401)
                .bodyFile(jsonResponseFile)
                .build();
        var paymentRequest = CreatePaymentRequest.builder().build();

        var paymentResponse = tlClient.payments().createPayment(paymentRequest);

        assertTrue(paymentResponse.isError());
        var expected = deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }

    @ParameterizedTest(name = "it should get a payment with status {0}")
    @ValueSource(strings = {"authorization_required", "settled", "failed"})
    public void shouldReturnASettledPayment(String status) {
        var jsonResponseFile = new StringBuilder("payments/200.get_payment_by_id.")
                .append(status)
                .append(".json")
                .toString();
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.builder()
                .method("get")
                .path(urlPathMatching("/payments/.*"))
                .withAuthorization()
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        var response = tlClient.payments().getPayment("a-payment-id");

        assertNotError(response);
        var expected = deserializeJsonFileTo(jsonResponseFile, BasePaymentDetail.class);
        assertEquals(expected, response.getData());
    }

    @Test
    @DisplayName("It should return an error if a payment is not found")
    public void shouldThrowIfPaymentNotFound() {
        var jsonResponseFile = "payments/404.payment_not_found.json";
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/payments"))
                .withAuthorization()
                .withSignature()
                .status(404)
                .bodyFile(jsonResponseFile)
                .build();
        var paymentRequest = CreatePaymentRequest.builder().build();

        var paymentResponse = tlClient.payments().createPayment(paymentRequest);

        assertTrue(paymentResponse.isError());
        var expected = deserializeJsonFileTo(jsonResponseFile, ProblemDetails.class);
        assertEquals(expected, paymentResponse.getError());
    }
}
