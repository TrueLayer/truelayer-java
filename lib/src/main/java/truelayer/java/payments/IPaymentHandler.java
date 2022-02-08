package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;

/**
 * Exposes all the payments related capabilities of the library.
 */
public interface IPaymentHandler {

    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request);

    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId);

    CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest request);

    CompletableFuture<ApiResponse<SubmitProviderSelectionResponse>> submitProviderSelection(
            String paymentId, SubmitProviderSelectionRequest submitProviderSelectionRequest);
}
