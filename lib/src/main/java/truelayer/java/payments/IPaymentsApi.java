package truelayer.java.payments;

import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.payments.entities.*;
import truelayer.java.payments.entities.paymentdetail.PaymentDetail;

/**
 * Exposes all the payments related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/create-payment"><i>Payments</i> API reference</a>
 */
public interface IPaymentsApi {

    /**
     * Initialises a payment resource.
     * @param request a create payment request payload
     * @return the response of the <i>Create Payment</i> operation
     * @see <a href="https://docs.truelayer.com/reference/create-payment"><i>Create Payment</i> API reference</a>
     */
    @POST("/payments")
    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(@Body CreatePaymentRequest request);

    /**
     * Gets a payment resource by id.
     * @param paymentId the payment identifier
     * @return the response of the <i>Get Payment</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-1"><i>Get Payment</i> API reference</a>
     */
    @GET("/payments/{id}")
    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(@Path("id") String paymentId);

    /**
     * Starts an authorization flow for a given payment resource.
     * @param paymentId the payment identifier
     * @param request a start authorization flow request payload
     * @return the response of the <i>Start Authorization Flow</i> operation
     * @see <a href="https://docs.truelayer.com/reference/start-payment-authorization-flow"><i>Start Authorization Flow</i> API reference</a>
     */
    @POST("/payments/{id}/authorization-flow")
    CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            @Path("id") String paymentId, @Body StartAuthorizationFlowRequest request);

    /**
     * Submit the provider selection for a given payment resource.
     * @param paymentId the payment identifier
     * @param request a submit provider selection request payload
     * @return the response of the <i>Submit Provider Selection</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-provider-selection"><i>Submit Provider Selection</i> API reference</a>
     */
    @POST("/payments/{id}/authorization-flow/actions/provider-selection")
    CompletableFuture<ApiResponse<SubmitProviderSelectionResponse>> submitProviderSelection(
            @Path("id") String paymentId, @Body SubmitProviderSelectionRequest request);
}
