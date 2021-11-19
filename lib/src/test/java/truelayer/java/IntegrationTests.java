package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.*;
import truelayer.java.payments.entities.CreatePaymentRequest;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TrueLayerClient.ConfigurationKeys.*;


@WireMockTest
@Tag("integration")
public class IntegrationTests {
    // todo: error testing to be improved as soon as we improve the error handling

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
    @DisplayName("It should throw an exception in case on an authorized error from the auth API.")
    public void shouldThrowIfUnauthorized() {
        stubFor(
                post("/connect/token").willReturn(
                        badRequest()
                                .withBodyFile("auth/400.invalid_client.json")
                )
        );

        var thrown = Assertions.assertThrows(TrueLayerException.class,
                () -> tlClient.auth().getOauthToken(List.of("paydirect")));

        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains("\"error\": \"invalid_client\""));
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

        var accessToken = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertNotNull(accessToken);
        assertFalse(accessToken.getAccessToken().isEmpty());
        assertFalse(accessToken.getTokenType().isEmpty());
        assertFalse(accessToken.getScope().isEmpty());
        assertTrue(accessToken.getExpiresIn() > 0);
    }

    @Test
    @DisplayName("It should create and return a payment")
    public void shouldCreateAndReturnAPayment() {
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                )
        );
        stubFor(
                post("/payments").willReturn(
                        ok().withBodyFile("payments/2xx.payment.json")
                )
        );

        var paymentRequest = CreatePaymentRequest.builder().build();
        var payment = tlClient.payments().createPayment(paymentRequest);

        assertNotNull(payment);
        assertFalse(payment.getPaymentId().isEmpty());
        assertFalse(payment.getStatus().isEmpty());
        assertFalse(payment.getResourceToken().isEmpty());
    }

    @Test
    @DisplayName("It should throw an exception if the signature is not valid")
    public void shouldThrowIfSignatureIsInvalid() {
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

        var thrown = Assertions.assertThrows(TrueLayerException.class,
                () -> tlClient.payments().createPayment(paymentRequest));

        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains("\"status\":401"));
        assertTrue(thrown.getMessage().contains("\"detail\":\"Invalid request signature.\""));
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
                        ok().withBodyFile("payments/2xx.payment.json")
                )
        );

        var payment = tlClient.payments().getPayment("a-payment-id");

        assertNotNull(payment);
        assertFalse(payment.getPaymentId().isEmpty());
        assertFalse(payment.getStatus().isEmpty());
        assertFalse(payment.getResourceToken().isEmpty());
    }

    @Test
    @DisplayName("It should throw an exception if a payment is not found")
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

        var thrown = Assertions.assertThrows(TrueLayerException.class,
                () -> tlClient.payments().createPayment(paymentRequest));

        assertNotNull(thrown);
        assertTrue(thrown.getMessage().contains("\"status\":404"));
        assertTrue(thrown.getMessage().contains("\"detail\":\"Payment could not be found.\""));
    }
}
