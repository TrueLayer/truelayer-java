package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import truelayer.java.TestUtils.RequestStub;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.MerchantAccount;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.Constants.ConfigurationKeys.*;


@WireMockTest
@Tag("integration")
public class IntegrationTests {
    public static final String LIBRARY_INFO = "truelayer-java/DEVELOPMENT";
    private final static String UUID_REGEX_PATTERN = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";
    private TrueLayerClient tlClient;

    @SneakyThrows
    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        tlClient = TrueLayerClient.builder()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .build();

        // don't try this at home
        var properties = new PropertiesConfiguration();
        properties.addProperty(AUTH_ENDPOINT_URL_LIVE, wireMockRuntimeInfo.getHttpBaseUrl());
        properties.addProperty(AUTH_ENDPOINT_URL_SANDBOX, wireMockRuntimeInfo.getHttpBaseUrl());
        properties.addProperty(PAYMENTS_ENDPOINT_URL_LIVE, wireMockRuntimeInfo.getHttpBaseUrl());
        properties.addProperty(PAYMENTS_ENDPOINT_URL_SANDBOX, wireMockRuntimeInfo.getHttpBaseUrl());
        properties.addProperty(PAYMENTS_SCOPES, "paydirect");
        writeField(tlClient, "configuration", properties, true);
    }

    @Test
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    public void shouldReturnErrorIfUnauthorized() {
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(400)
                .bodyFile("auth/400.invalid_client.json")
                .build();

        var response = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertTrue(response.isError());
        assertEquals("error", response.getError().getType());
        assertTrue(response.getError().getDetail().contains("invalid_client"));
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create and return an access token")
    public void shouldReturnAnAccessToken() {
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .build();

        var response = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertFalse(response.isError());
        assertFalse(response.getData().getAccessToken().isEmpty());
        assertFalse(response.getData().getTokenType().isEmpty());
        assertFalse(response.getData().getScope().isEmpty());
        assertTrue(response.getData().getExpiresIn() > 0);
    }

    @Test
    @DisplayName("It should create and return a payment with merchant account as beneficiary")
    public void shouldCreateAndReturnAPaymentMerchantAccount() {
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
                .bodyFile("payments/2xx.payment.merchant_account.json")
                .build();
        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

        var response = tlClient.payments().createPayment(paymentRequest);

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @DisplayName("It should create and return a payment with external account as beneficiary")
    public void shouldCreateAndReturnAPaymentExternalAccount() {
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
                .bodyFile("payments/2xx.payment.external_account.json")
                .build();
        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

        var response = tlClient.payments().createPayment(paymentRequest);

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @DisplayName("It should return an error if the signature is not valid")
    public void shouldReturnErrorIfSignatureIsInvalid() {
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
                .bodyFile("payments/401.invalid_signature.json")
                .build();
        var paymentRequest = CreatePaymentRequest.builder().build();

        var paymentResponse = tlClient.payments().createPayment(paymentRequest);

        assertTrue(paymentResponse.isError());
        assertEquals(401, paymentResponse.getError().getStatus());
        assertEquals("Invalid request signature.", paymentResponse.getError().getDetail());
        assertEquals("https://docs.truelayer.com/docs/error-types#unauthenticated", paymentResponse.getError().getType());
        assertEquals("Unauthenticated", paymentResponse.getError().getTitle());
        assertFalse(paymentResponse.getError().getTraceId().isEmpty());
    }

    @Test
    @DisplayName("It should get a payment")
    public void shouldReturnAPayment() {
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
                .bodyFile("payments/2xx.payment.merchant_account.json")
                .build();

        var response = tlClient.payments().getPayment("a-payment-id");

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @DisplayName("It should return an error if a payment is not found")
    public void shouldThrowIfPaymentNotFound() {
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
                .bodyFile("payments/404.payment_not_found.json")
                .build();

        var paymentRequest = CreatePaymentRequest.builder().build();

        var response = tlClient.payments().createPayment(paymentRequest);

        assertTrue(response.isError());
        assertEquals(404, response.getError().getStatus());
        assertEquals("Payment could not be found.", response.getError().getDetail());
        assertEquals("https://docs.truelayer.com/docs/error-types#not-found", response.getError().getType());
        assertEquals("Not Found", response.getError().getTitle());
        assertFalse(response.getError().getTraceId().isEmpty());
    }
}
