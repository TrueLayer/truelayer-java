package truelayer.java.payments;

import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;
import java.net.URISyntaxException;

public interface IPayments {

    Payment createPayment(CreatePaymentRequest request) throws IOException, URISyntaxException, InterruptedException, AuthenticationException;
}
