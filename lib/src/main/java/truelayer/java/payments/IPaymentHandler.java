package truelayer.java.payments;

import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

import java.net.URI;

public interface IPaymentHandler {

    ApiResponse<Payment> createPayment(CreatePaymentRequest request);

    ApiResponse<Payment> getPayment(String paymentId);
}
