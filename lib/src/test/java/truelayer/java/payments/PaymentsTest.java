package truelayer.java.payments;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentsTest {

    private static Payments payments;

    @BeforeAll
    static void init() {
        payments = new Payments();
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