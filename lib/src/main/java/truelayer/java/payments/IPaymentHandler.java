package truelayer.java.payments;

import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;

public interface IPaymentHandler {

    ApiResponse<Payment> createPayment(CreatePaymentRequest request);

    ApiResponse<Payment> getPayment(String paymentId);

    ApiResponse<AuthorizationFlowResponse> startAuthorizationFlow(
            String paymentId,
            StartAuthorizationFlowRequest startAuthorizationFlowRequest);

    ApiResponse<AuthorizationFlowResponse> submitProviderSelection(
            String paymentId,
            SubmitProviderSelectionRequest submitProviderSelectionRequest);
}
