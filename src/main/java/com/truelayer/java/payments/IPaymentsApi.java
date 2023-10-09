package com.truelayer.java.payments;

import com.truelayer.java.entities.EmptyRequestBody;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

/**
 * Exposes all the payments related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/create-payment"><i>Payments</i> API reference</a>
 */
public interface IPaymentsApi {
    /**
     * Initialises a payment resource.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param headers map representing custom HTTP headers to be sent
     * @param request a create payment request payload
     * @return the response of the <i>Create Payment</i> operation
     * @see <a href="https://docs.truelayer.com/reference/create-payment"><i>Create Payment</i> API reference</a>
     */
    @POST("/payments")
    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(
            @Tag RequestScopes scopes, @HeaderMap Map<String, String> headers, @Body CreatePaymentRequest request);

    /**
     * Gets a payment resource by id.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param paymentId the payment identifier
     * @return the response of the <i>Get Payment</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-1"><i>Get Payment</i> API reference</a>
     */
    @GET("/payments/{id}")
    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(@Tag RequestScopes scopes, @Path("id") String paymentId);

    /**
     * Starts an authorization flow for a given payment resource.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param headers map representing custom HTTP headers to be sent
     * @param paymentId the payment identifier
     * @param request a start authorization flow request payload
     * @return the response of the <i>Start Authorization Flow</i> operation
     * @see <a href="https://docs.truelayer.com/reference/start-payment-authorization-flow"><i>Start Authorization Flow</i> API reference</a>
     */
    @POST("/payments/{id}/authorization-flow")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            @Tag RequestScopes scopes,
            @HeaderMap Map<String, String> headers,
            @Path("id") String paymentId,
            @Body StartAuthorizationFlowRequest request);

    /**
     * Submit the provider selection for a given payment resource.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param headers map representing custom HTTP headers to be sent
     * @param paymentId the payment identifier
     * @param request a submit provider selection request payload
     * @return the response of the <i>Submit Provider Selection</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-provider-selection"><i>Submit Provider Selection</i> API reference</a>
     */
    @POST("/payments/{id}/authorization-flow/actions/provider-selection")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            @Tag RequestScopes scopes,
            @HeaderMap Map<String, String> headers,
            @Path("id") String paymentId,
            @Body SubmitProviderSelectionRequest request);

    /**
     * Submit consent collected from the PSU for a given payment resource.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param headers map representing custom HTTP headers to be sent
     * @param paymentId the payment identifier
     * @param request a submit consent request payload
     * @return the response of the <i>Submit Consent</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-consent"><i>Submit Consent</i> API reference</a>
     */
    @POST("/payments/{id}/authorization-flow/actions/consent")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(
            @Tag RequestScopes scopes,
            @HeaderMap Map<String, String> headers,
            @Path("id") String paymentId,
            @Body EmptyRequestBody request);

    /**
     * Submit form inputs collected from the PSU for a given payment resource.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param headers map representing custom HTTP headers to be sent
     * @param paymentId the payment identifier
     * @param request a submit form request payload
     * @return the response of the <i>Submit Form</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-form"><i>Submit Form</i> API reference</a>
     */
    @POST("/payments/{id}/authorization-flow/actions/form")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(
            @Tag RequestScopes scopes,
            @HeaderMap Map<String, String> headers,
            @Path("id") String paymentId,
            @Body SubmitFormRequest request);

    /**
     * Refund a merchant account payment.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param headers map representing custom HTTP headers to be sent
     * @param paymentId the payment identifier
     * @param request a create refund request payload
     * @return the response of the <i>Create Payment Refund</i> operation
     * @see <a href="https://docs.truelayer.com/reference/create-payment-refund"><i>Create Payment Refund</i> API reference</a>
     */
    @POST("/payments/{id}/refunds")
    CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            @Tag RequestScopes scopes,
            @HeaderMap Map<String, String> headers,
            @Path("id") String paymentId,
            @Body CreatePaymentRefundRequest request);

    /**
     * Returns all refunds of a payment.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param paymentId the payment identifier
     * @return the response of the <i>Get Payment Refunds</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-refunds"><i>Get Payment Refunds</i> API reference</a>
     */
    @GET("/payments/{id}/refunds")
    CompletableFuture<ApiResponse<ListPaymentRefundsResponse>> listPaymentRefunds(
            @Tag RequestScopes scopes, @Path("id") String paymentId);

    /**
     * Returns refund details.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param paymentId the payment identifier
     * @return the response of the <i>Get Payment Refund</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payment-refund"><i>Get Payment Refund</i> API reference</a>
     */
    @GET("/payments/{paymentId}/refunds/{refundId}")
    CompletableFuture<ApiResponse<PaymentRefund>> getPaymentRefundById(
            @Tag RequestScopes scopes, @Path("paymentId") String paymentId, @Path("refundId") String refundId);
}
