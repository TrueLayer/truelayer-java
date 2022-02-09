package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;

/**
 * Exposes all the payments related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/create-payment"><i>Payments</i> API reference</a>
 */
public interface IPaymentHandler {

    /**
     * Initialises a payment resource.
     * @param request a create payment request payload
     * @return the response of the <i>Create Payment</i> operation
     * @see <a href="https://docs.truelayer.com/reference/create-payment"><i>Create Payment</i> API reference</a>
     */
    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request);

    /**
     * Gets a payment resource by id.
     * @param paymentId the payment identifier
     * @return the response of the <i>Get Payment</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-1"><i>Get Payment</i> API reference</a>
     */
    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId);

    /**
     * Starts an authorization flow for a given payment resource.
     * @param paymentId the payment identifier
     * @param request a start authorization flow request payload
     * @return the response of the <i>Start Authorization Flow</i> operation
     * @see <a href="https://docs.truelayer.com/reference/start-payment-authorization-flow"><i>Start Authorization Flow</i> API reference</a>
     */
    CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest request);

    /**
     * Submit the provider selection for a given payment resource.
     * @param paymentId the payment identifier
     * @param submitProviderSelectionRequest a submit provider selection request payload
     * @return the response of the <i>Submit Provider Selection</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-provider-selection"><i>Submit Provider Selection</i> API reference</a>
     */
    CompletableFuture<ApiResponse<SubmitProviderSelectionResponse>> submitProviderSelection(
            String paymentId, SubmitProviderSelectionRequest submitProviderSelectionRequest);
}
