package truelayer.java.payments;

import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.GetPaymentByIdResponse;

public interface IPaymentHandler {

    ApiResponse<CreatePaymentResponse> createPayment(CreatePaymentRequest request);

    ApiResponse<GetPaymentByIdResponse> getPayment(String paymentId);
}
