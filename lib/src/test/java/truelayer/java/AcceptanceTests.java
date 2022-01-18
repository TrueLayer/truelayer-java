package truelayer.java;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.*;
import truelayer.java.payments.entities.paymentmethod.BankTransfer;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;

import java.nio.charset.StandardCharsets;

import static truelayer.java.TestUtils.assertNotError;

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
    @DisplayName("It should create a payment")
    public void shouldCreateAPayment() {
        var paymentRequest = buildPaymentRequest();

        var createPaymentResponse = tlClient.payments().createPayment(paymentRequest);

        assertNotError(createPaymentResponse);
    }

    @Test
    @DisplayName("It should get a payment by id")
    @SneakyThrows
    public void shouldGetAPaymentById() {
        var paymentRequest = buildPaymentRequest();
        var createPaymentResponse = tlClient.payments().createPayment(paymentRequest);
        assertNotError(createPaymentResponse);

        var getPaymentByIdResponse = tlClient.payments().getPayment(createPaymentResponse.getData().getId());

        assertNotError(getPaymentByIdResponse);
    }

    private CreatePaymentRequest buildPaymentRequest() {
        return CreatePaymentRequest.builder()
                .amountInMinor(101)
                .currency("GBP")
                .paymentMethod(BankTransfer.builder()
                        .build())
                .beneficiary(MerchantAccount.builder()
                        .id("e83c4c20-b2ad-4b73-8a32-ee855362d72a")
                        .build())
                .user(CreatePaymentRequest.User.builder()
                        .name("Andrea Di Lisio")
                        .type(CreatePaymentRequest.User.Type.NEW)
                        .email("andrea@truelayer.com")
                        .build())
                .build();
    }
}
