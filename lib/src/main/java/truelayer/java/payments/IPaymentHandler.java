package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;

public interface IPaymentHandler {

    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request);

    CompletableFuture<ApiResponse<BasePaymentDetail>> getPayment(String paymentId);
}
