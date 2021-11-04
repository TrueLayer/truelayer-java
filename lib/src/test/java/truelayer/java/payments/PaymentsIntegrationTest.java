package truelayer.java.payments;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import truelayer.java.TestsUtil;
import truelayer.java.auth.Authentication;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;
import truelayer.java.payments.exception.PaymentsException;

import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsIntegrationTest {

    //todo these are integration tests, we need to mock the external dependency

    private static final String A_CLIENT_ID = "giulioleso-8993c9";
    private static final String A_SECRET = "66a627c7-abbc-4f9e-9f7c-87673c5b896e";

    private static Payments payments;

    @BeforeAll
    static void init() {
        Authentication authentication = Authentication.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .build();

        payments = Payments.builder()
                .authentication(authentication)
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .restTemplate(new RestTemplate())
                .build();
    }

    @Test
    void createPayment() throws IOException, AuthenticationException, ParseException, JOSEException {
        CreatePaymentRequest createPaymentRequest = TestsUtil.getCreatePaymentRequest();

        Payment payment = payments.createPayment(createPaymentRequest);

        assertNotNull(payment.getPaymentId());
    }

    @Test
    void createAndRetrieveAPayment() throws IOException, AuthenticationException, ParseException, JOSEException {
        CreatePaymentRequest createPaymentRequest = TestsUtil.getCreatePaymentRequest();

        Payment createdPayment = payments.createPayment(createPaymentRequest);

        assertNotNull(createdPayment);

        Payment retrievedPayment = payments.getPayment(createdPayment.getPaymentId());

        assertEquals(createdPayment.getPaymentId(), retrievedPayment.getPaymentId());
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