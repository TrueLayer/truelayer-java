package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import lombok.Value;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.CreatePaymentRequest;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import truelayer.java.payments.entities.StartAuthorizationFlowResponse;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;

@Value
public class PaymentHandler implements IPaymentHandler {
    IPaymentsApi paymentsApi;

    public static PaymentHandlerBuilder New() {
        return new PaymentHandlerBuilder();
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(
            CreatePaymentRequest createPaymentRequest) {
        return paymentsApi.createPayment(createPaymentRequest);
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId) {
        return paymentsApi.getPayment(paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest startAuthorizationFlowRequest) {
        return paymentsApi.startAuthorizationFlow(paymentId, startAuthorizationFlowRequest);
    }
}
