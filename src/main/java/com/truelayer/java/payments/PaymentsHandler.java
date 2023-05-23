package com.truelayer.java.payments;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

@Value
public class PaymentsHandler implements IPaymentsHandler {

    IPaymentsApi paymentsApi;

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request) {
        return paymentsApi.createPayment(emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(
            Headers headers, CreatePaymentRequest request) {
        return paymentsApi.createPayment(toMap(headers), request);
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId) {
        return paymentsApi.getPayment(paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest request) {
        return paymentsApi.startAuthorizationFlow(emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            Headers headers, String paymentId, StartAuthorizationFlowRequest request) {
        return paymentsApi.startAuthorizationFlow(toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String paymentId, SubmitProviderSelectionRequest request) {
        return paymentsApi.submitProviderSelection(emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            Headers headers, String paymentId, SubmitProviderSelectionRequest request) {
        return paymentsApi.submitProviderSelection(toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(
            String paymentId, SubmitConsentRequest request) {
        return paymentsApi.submitConsent(emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(
            Headers headers, String paymentId, SubmitConsentRequest request) {
        return paymentsApi.submitConsent(toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(
            String paymentId, SubmitFormRequest request) {
        return paymentsApi.submitForm(emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(
            Headers headers, String paymentId, SubmitFormRequest request) {
        return paymentsApi.submitForm(toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            String paymentId, CreatePaymentRefundRequest request) {
        return paymentsApi.createPaymentRefund(emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            Headers headers, String paymentId, CreatePaymentRefundRequest request) {
        return paymentsApi.createPaymentRefund(toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<ListPaymentRefundsResponse>> listPaymentRefunds(String paymentId) {
        return paymentsApi.listPaymentRefunds(paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentRefund>> getPaymentRefundById(String paymentId, String refundId) {
        return paymentsApi.getPaymentRefundById(paymentId, refundId);
    }
}
