package truelayer.java.payments;

import org.junit.jupiter.api.Test;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentsTest {

    @Test
    void createPayment() throws IOException, URISyntaxException, InterruptedException {
        Payments payments = new Payments();
        CreatePaymentRequest createPaymentRequest = getCreatePaymentRequest();

        Payment payment = payments.createPayment(createPaymentRequest);

        assertNotNull(payment.getPaymentId());
    }

    @Test
    void getePayment() {
        Payments payments = new Payments();

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

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setAmountInMinor(1);
        createPaymentRequest.setCurrency("GBP");
        createPaymentRequest.setPaymentMethod(paymentMethod);
        createPaymentRequest.setBeneficiary(paymentBeneficiary);
        createPaymentRequest.setUser(paymentUser);
        return createPaymentRequest;
    }
}