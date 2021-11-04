package truelayer.java.payments;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import truelayer.java.SigningOptions;
import truelayer.java.auth.Authentication;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.payments.exception.PaymentsException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsIntegrationTest {

    //todo these are integration tests, we need to mock the external dependency

    private static final String A_CLIENT_ID = "<a_client_id>";
    private static final String A_SECRET = "<a_client_secret>";
    private static final String A_KEY_ID = "<a_key_id>";

    private static Payments payments;

    @BeforeAll
    static void init() throws IOException {
        var signingOptions = SigningOptions.builder()
                .privateKey(Files.readAllBytes(Path.of("src/test/resources/ec512-private-key.pem")))
                .keyId(A_KEY_ID)
                .build();

        Authentication authentication = Authentication.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .build();

        payments = Payments.builder()
                .authentication(authentication)
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .signingOptions(signingOptions)
                .build();
    }

    @Test
    void createPayment() throws IOException, AuthenticationException, ParseException, JOSEException {
        var paymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(101)
                .currency("GBP")
                .paymentMethod(CreatePaymentRequest.Method.builder()
                        .type("bank_transfer")
                        .build())
                .beneficiary(CreatePaymentRequest.Beneficiary.builder()
                        .type("merchant_account")
                        .name("Luca")
                        .id(UUID.randomUUID().toString())
                        .build())
                .user(CreatePaymentRequest.User.builder()
                        .name("Andrea")
                        .type("new")
                        .build())
                .build();

        Payment payment = payments.createPayment(paymentRequest);

        assertNotNull(payment.getPaymentId());
    }

    @Test
    void createAndRetrieveAPayment() throws IOException, AuthenticationException, ParseException, JOSEException {
        // Given
        var paymentRequest = CreatePaymentRequest.builder()
                .amountInMinor(101)
                .currency("GBP")
                .paymentMethod(CreatePaymentRequest.Method.builder()
                        .type("bank_transfer")
                        .build())
                .beneficiary(CreatePaymentRequest.Beneficiary.builder()
                        .type("merchant_account")
                        .name("Luca")
                        .id(UUID.randomUUID().toString())
                        .build())
                .user(CreatePaymentRequest.User.builder()
                        .name("Andrea")
                        .type("new")
                        .build())
                .build();
        var payment = payments.createPayment(paymentRequest);

        //When
        Payment retrievedPayment = payments.getPayment(payment.getPaymentId());

        //Then
        assertEquals(payment.getPaymentId(), retrievedPayment.getPaymentId());
    }

    @Test
    void getPaymentExceptionWhenTryingToGetMissingPayment() {
        //when
        PaymentsException exception = assertThrows(PaymentsException.class, () -> {
            payments.getPayment("1");
        });

        assertEquals(exception.getErrorCode(), "GENERIC_ERROR");
        assertTrue(exception.getErrorMessage().startsWith("404 Not Found"));
    }


}