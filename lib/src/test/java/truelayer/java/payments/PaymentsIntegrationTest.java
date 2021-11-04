package truelayer.java.payments;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import truelayer.java.auth.Authentication;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentsIntegrationTest {

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
    void createPayment() throws IOException, AuthenticationException {
        CreatePaymentRequest createPaymentRequest = getCreatePaymentRequest();

        Payment payment = payments.createPayment(createPaymentRequest);

        assertNotNull(payment.getPaymentId());
    }

    @Test
    void createAndRetrieveAPayment() throws IOException, AuthenticationException {
        CreatePaymentRequest createPaymentRequest = getCreatePaymentRequest();

        Payment createdPayment = payments.createPayment(createPaymentRequest);

        assertNotNull(createdPayment);

        Payment retrievedPayment = payments.getPayment(createdPayment.getPaymentId());

        assertEquals(createdPayment.getPaymentId(), retrievedPayment.getPaymentId());
    }

    @Test
    void getPayment() throws AuthenticationException {
        Payment payment = payments.getPayment("1");

        assertNotNull(payment.getPaymentId());
    }

    private CreatePaymentRequest getCreatePaymentRequest() {
        CreatePaymentRequest.PaymentMethod paymentMethod =
                new CreatePaymentRequest.PaymentMethod("bank_transfer");
        CreatePaymentRequest.PaymentBeneficiary paymentBeneficiary =
                new CreatePaymentRequest.PaymentBeneficiary("merchant_account", "c54104a5-fdd1-4277-8793-dbfa511c898b");
        CreatePaymentRequest.PaymentUser paymentUser =
                new CreatePaymentRequest.PaymentUser("new", "Giulio Leso");
        paymentUser.setEmail("g@gmail.com");

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setAmountInMinor(1);
        createPaymentRequest.setCurrency("GBP");
        createPaymentRequest.setPaymentMethod(paymentMethod);
        createPaymentRequest.setBeneficiary(paymentBeneficiary);
        createPaymentRequest.setUser(paymentUser);
        return createPaymentRequest;
    }
}