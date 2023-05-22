package com.truelayer.java.payments;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import java.util.concurrent.CompletableFuture;

/**
 * Provides /payments API integration without the burden of Retrofit's annotation
 * and improve both usability and backward compatibility for the implemented endpoints.
 */
public interface IPaymentsHandler {

    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request);

    CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(Headers headers, CreatePaymentRequest request);

    CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            Headers headers, String paymentId, StartAuthorizationFlowRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String paymentId, SubmitProviderSelectionRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            Headers headers, String paymentId, SubmitProviderSelectionRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(
            String paymentId, SubmitConsentRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(
            Headers headers, String paymentId, SubmitConsentRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(String paymentId, SubmitFormRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(
            Headers headers, String paymentId, SubmitFormRequest request);

    CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            String paymentId, CreatePaymentRefundRequest request);

    CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            Headers headers, String paymentId, CreatePaymentRefundRequest request);

    CompletableFuture<ApiResponse<ListPaymentRefundsResponse>> listPaymentRefunds(String paymentId);

    CompletableFuture<ApiResponse<PaymentRefund>> getPaymentRefundById(String paymentId, String refundId);
}
