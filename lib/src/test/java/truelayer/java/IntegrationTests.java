package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.*;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.MerchantAccount;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.ConfigurationKeys.*;


@WireMockTest
@Tag("integration")
public class IntegrationTests {
    public static final String LIBRARY_INFO = "truelayer-java/DEVELOPMENT";
    private TrueLayerClient tlClient;

    @SneakyThrows
    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        tlClient = TrueLayerClient.builder()

                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
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
    @DisplayName("It should call every endpoint with a User-Agent header containing library info.")
    public void shouldCallAnEndpointWithUserAgent(){
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                post("/payments").willReturn(
                        ok().withBodyFile("payments/2xx.payment.merchant_account.json")
                )
        );
        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

        tlClient.payments().createPayment(paymentRequest);

        verify(postRequestedFor(urlEqualTo("/connect/token"))
                .withHeader("User-Agent", equalTo(LIBRARY_INFO)));
        verify(postRequestedFor(urlEqualTo("/payments"))
                .withHeader("User-Agent", equalTo(LIBRARY_INFO)));
    }

    @Test
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    public void shouldReturnErrorIfUnauthorized() {
        stubFor(
                post("/connect/token").willReturn(
                        badRequest()
                                .withBodyFile("auth/400.invalid_client.json")
                )
        );

        var response = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertTrue(response.isError());
        assertTrue(response.getError().getTitle().contains("\"error\": \"invalid_client\""));
    }

    @SneakyThrows
    @Test
    @DisplayName("It should create and return an access token")
    public void shouldReturnAnAccessToken() {
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );

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
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                post("/payments").willReturn(
                        ok().withBodyFile("payments/2xx.payment.merchant_account.json")
                )
        );

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
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                post("/payments").willReturn(
                        ok().withBodyFile("payments/2xx.payment.external_account.json")
                )
        );

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
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                post("/payments").willReturn(
                        unauthorized().withBodyFile("payments/401.invalid_signature.json")
                )
        );
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
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                get(urlPathMatching("/payments/.*")).willReturn(
                        ok().withBodyFile("payments/2xx.payment.merchant_account.json")
                )
        );

        var response = tlClient.payments().getPayment("a-payment-id");

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @DisplayName("It should return an error if a payment is not found")
    public void shouldThrowIfPaymentNotFound() {
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                post("/payments").willReturn(
                        unauthorized().withBodyFile("payments/404.payment_not_found.json")
                )
        );
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
