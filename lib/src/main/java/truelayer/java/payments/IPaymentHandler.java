package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import truelayer.java.payments.entities.StartAuthorizationFlowResponse;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;

public interface IPaymentHandler {

    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request);

    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId);

    CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest request);
}
