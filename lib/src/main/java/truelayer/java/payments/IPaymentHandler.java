package truelayer.java.payments;

import com.nimbusds.jose.JOSEException;
import truelayer.java.TrueLayerException;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;

public interface IPaymentHandler {

    Payment createPayment(CreatePaymentRequest request);

    Payment getPayment(String paymentId);
}
