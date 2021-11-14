package truelayer.java.payments;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.payments.exception.PaymentException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.getClientCredentialsOptions;

//todo review this completely
@Disabled
class PaymentsIntegrationTest {
    //todo these are integration tests, we need to mock the external dependency

    private static final String A_CLIENT_ID = "<a_client_id>";
    private static final String A_SECRET = "<a_client_secret>";
    private static final String A_KEY_ID = "<a_key_id>";

    private static PaymentHandler payments;

    @BeforeAll
    static void init() throws IOException {
       /* var signingOptions = SigningOptions.builder()
                .privateKey(Files.readAllBytes(Path.of("src/test/resources/ec512-private-key.pem")))
                .keyId(A_KEY_ID)
                .build();
        TrueLayerHttpClient trueLayerHttpClient = TrueLayerHttpClient.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .httpClient(HttpClient.newHttpClient())
                .build();*/

        /*Authentication authentication = Authentication.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .build();*/

       /* payments = Payments.builder()
                .authentication(authentication)
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .signingOptions(signingOptions)
                .build();*/
    }

    @Test
    void getToken() throws AuthenticationException, IOException {
        var authenticationHandler = AuthenticationHandler.builder()
                .authenticationApi((clientId, clientSecret, grantType, scopes) -> null)
                .clientCredentialsOptions(getClientCredentialsOptions())
                .build();


        var oauthToken = authenticationHandler.getOauthToken(List.of("paydirect"));
        Assertions.assertEquals("", oauthToken.getAccessToken());
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
                        //.name("Luca") todo: by setting this we broke the signature. involve paymetns api
                        .id(UUID.randomUUID().toString())
                        .build())
                .user(CreatePaymentRequest.User.builder()
                        .name("Andrea")
                        .type("new")
                        .email("andrea@truelayer.com") // if we don't set this one we get a signature check failure. todo: provide feedback to payments api
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
        PaymentException exception = assertThrows(PaymentException.class, () -> {
            payments.getPayment("1");
        });

        assertEquals(exception.getErrorCode(), "GENERIC_ERROR");
        assertTrue(exception.getErrorMessage().startsWith("404 Not Found"));
    }


}