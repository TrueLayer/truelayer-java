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
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.GetPaymentByIdResponse;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.Constants.ConfigurationKeys.*;
import static truelayer.java.TestUtils.assertNotError;
import static truelayer.java.TestUtils.deserializeJsonFileTo;


@WireMockTest
@Tag("integration")
public class IntegrationTests {
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
        var jsonResponseFile = "auth/200.access_token.json";
        RequestStub.builder()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        var response = tlClient.auth().getOauthToken(List.of("paydirect"));

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

    @Test
    @DisplayName("It should get a payment")
    public void shouldReturnAPayment() {
        var jsonResponseFile = "payments/200.get_payment_by_id.json";
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
        var expected = deserializeJsonFileTo(jsonResponseFile, GetPaymentByIdResponse.class);
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
