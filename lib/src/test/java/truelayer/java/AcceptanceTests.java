package truelayer.java;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import lombok.SneakyThrows;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.*;
import truelayer.java.TestUtils.RequestStub;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.MerchantAccount;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.*;

@Tag("acceptance")
public class AcceptanceTests {
    private static TrueLayerClient tlClient;

    @BeforeAll
    public static void setup() {
        tlClient = TrueLayerClient.builder()
                .useSandbox()
                .clientCredentials(
                    ClientCredentials
                        .builder()
                            .clientId(System.getenv("TL_CLIENT_ID"))
                            .clientSecret(System.getenv("TL_CLIENT_SECRET"))
                        .build()
                )
                .signingOptions(TestUtils.getSigningOptions())
                .build();
    }

    @Test
    @Disabled
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    public void shouldReturnErrorIfUnauthorized() {
        var response = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertTrue(response.isError());
        assertTrue(response.getError().getTitle().contains("\"error\": \"invalid_client\""));
    }

    @Test
    @DisplayName("It should create and return an access token")
    public void shouldReturnAnAccessToken() {
        var response = tlClient.auth().getOauthToken(List.of("paydirect"));

        assertFalse(response.isError());
        assertFalse(response.getData().getAccessToken().isEmpty());
        assertFalse(response.getData().getTokenType().isEmpty());
        assertFalse(response.getData().getScope().isEmpty());
        assertTrue(response.getData().getExpiresIn() > 0);
    }

    @Test
    @Disabled
    @DisplayName("It should create and return a payment with merchant account as beneficiary")
    public void shouldCreateAndReturnAPaymentMerchantAccount() {

        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

        var response = tlClient.payments().createPayment(paymentRequest);

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @Disabled
    @DisplayName("It should create and return a payment with external account as beneficiary")
    public void shouldCreateAndReturnAPaymentExternalAccount() {
        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();

        var response = tlClient.payments().createPayment(paymentRequest);

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @Disabled
    @DisplayName("It should return an error if the signature is not valid")
    public void shouldReturnErrorIfSignatureIsInvalid() {
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
    @Disabled
    @DisplayName("It should get a payment")
    public void shouldReturnAPayment() {
        var response = tlClient.payments().getPayment("a-payment-id");

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }

    @Test
    @Disabled
    @DisplayName("It should return an error if a payment is not found")
    public void shouldThrowIfPaymentNotFound() {
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
