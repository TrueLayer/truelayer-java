package truelayer.java;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.IApiCallback;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;

import java.net.SocketException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TrueLayerClient.ConfigurationKeys.*;


@WireMockTest
@Tag("integration")
public class IntegrationTests {
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
    @SneakyThrows
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    public void shouldReturnErrorIfUnauthorized() {
        stubFor(
                post("/connect/token").willReturn(
                        badRequest()
                                .withBodyFile("auth/400.invalid_client.json")
                )
        );

        var response = tlClient.auth().getOauthToken(List.of("paydirect")).get();

        assertTrue(response.isError());
        assertTrue(response.getError().getTitle().contains("\"error\": \"invalid_client\""));
    }

    @Test
    @DisplayName("It should timeout while getting an access token")
    public void shouldTimeoutWhileGettingAnAccessToken() {
        stubFor(
                post("/connect/token").willReturn(
                        ok().withBodyFile("auth/200.access_token.json")
                                .withFixedDelay(3000)
                )

        );

        assertThrows(TimeoutException.class, () -> tlClient.auth().getOauthToken(List.of("paydirect"))
                .get(1000, TimeUnit.MILLISECONDS));
    }

    @Test
    @SneakyThrows
    @DisplayName("It should fail due to a connection reset")
    public void shouldFailDueToConnectionReset() {
        stubFor(
                post("/connect/token").willReturn(
                        aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)
                )

        );

        var thrown = assertThrows(ExecutionException.class, () -> tlClient.auth().getOauthToken(List.of("paydirect")).get());
        assertEquals(SocketException.class, thrown.getCause().getClass());
        assertEquals("Connection reset", thrown.getCause().getMessage());
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

        //var response = tlClient.auth().getOauthToken(List.of("paydirect"));

        var future = tlClient.auth().getOauthToken(List.of("paydirect")).whenComplete((response, throwable)->{
            assertFalse(response.isError());
            assertFalse(response.getData().getAccessToken().isEmpty());
            assertFalse(response.getData().getTokenType().isEmpty());
            assertFalse(response.getData().getScope().isEmpty());
            assertTrue(response.getData().getExpiresIn() > 0);

        });

        while(!future.isDone()){}

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
        var response = tlClient.payments().createPayment(paymentRequest);

        assertFalse(response.isError());
        assertFalse(response.getData().getPaymentId().isEmpty());
        assertFalse(response.getData().getStatus().isEmpty());
        assertFalse(response.getData().getResourceToken().isEmpty());
    }

    @Test
    @DisplayName("It should an error if the signature is not valid")
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
                        ok().withBodyFile("payments/2xx.payment.json")
                )
        );

        var response = tlClient.payments().getPayment("a-payment-id");

        assertFalse(response.isError());
        assertFalse(response.getData().getPaymentId().isEmpty());
        assertFalse(response.getData().getStatus().isEmpty());
        assertFalse(response.getData().getResourceToken().isEmpty());
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
