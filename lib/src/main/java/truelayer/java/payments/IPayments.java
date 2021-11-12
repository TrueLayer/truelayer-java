package truelayer.java.payments;

import com.nimbusds.jose.JOSEException;
import truelayer.java.auth.exceptions.AuthenticationException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public interface IPayments {

    Payment createPayment(CreatePaymentRequest request) throws IOException, URISyntaxException, InterruptedException, AuthenticationException, ParseException, JOSEException;
}
