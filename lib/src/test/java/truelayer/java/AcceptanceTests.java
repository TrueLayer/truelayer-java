package truelayer.java;

import org.junit.jupiter.api.*;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.MerchantAccount;

import java.nio.charset.StandardCharsets;
import java.util.List;

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
                .signingOptions(SigningOptions.builder()
                        .keyId(System.getenv("TL_SIGNING_KEY_ID"))
                        .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY").getBytes(StandardCharsets.UTF_8))
                        .build())
                .build();
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
    @DisplayName("It should create a payment")
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
    @DisplayName("It should create a payment and fetch by id soon after")
    public void shouldCreateAndReturnAPaymentExternalAccount() {
        var paymentRequest = CreatePaymentRequest.builder()
                .beneficiary(MerchantAccount.builder().build())
                .build();
        var createPaymentResponse = tlClient.payments().createPayment(paymentRequest);
        assertFalse(createPaymentResponse.isError());

        var response = tlClient.payments().getPayment(createPaymentResponse.getData().getId());

        assertFalse(response.isError());
        assertFalse(response.getData().getId().isEmpty());
        assertFalse(response.getData().getPaymentToken().isEmpty());
    }
}
