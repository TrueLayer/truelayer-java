package truelayer.java.payments;

import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.Payment;

public interface IPaymentHandler {

    ApiResponse<Payment> createPayment(CreatePaymentRequest request);

    ApiResponse<Payment> getPayment(String paymentId);
}
