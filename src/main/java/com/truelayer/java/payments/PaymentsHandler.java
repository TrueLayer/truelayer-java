package com.truelayer.java.payments;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.IAuthenticatedHandler;
import com.truelayer.java.entities.EmptyRequestBody;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.payments.entities.*;
import com.truelayer.java.payments.entities.paymentdetail.PaymentDetail;
import com.truelayer.java.payments.entities.paymentrefund.PaymentRefund;
import java.util.concurrent.CompletableFuture;
import lombok.Builder;

@Builder
public class PaymentsHandler implements IAuthenticatedHandler, IPaymentsHandler {

    private IPaymentsApi paymentsApi;

    @Builder.Default
    private RequestScopes scopes = RequestScopes.builder().scope(PAYMENTS).build();

    @Override
    public RequestScopes getRequestScopes() {
        return scopes;
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(CreatePaymentRequest request) {
        return paymentsApi.createPayment(getRequestScopes(), emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentResponse>> createPayment(
            Headers headers, CreatePaymentRequest request) {
        return paymentsApi.createPayment(getRequestScopes(), toMap(headers), request);
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentDetail>> getPayment(String paymentId) {
        return paymentsApi.getPayment(getRequestScopes(), paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> cancelPayment(String paymentId) {
        return paymentsApi.cancelPayment(getRequestScopes(), emptyMap(), paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> cancelPayment(Headers headers, String paymentId) {
        return paymentsApi.cancelPayment(getRequestScopes(), toMap(headers), paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String paymentId, StartAuthorizationFlowRequest request) {
        return paymentsApi.startAuthorizationFlow(getRequestScopes(), emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            Headers headers, String paymentId, StartAuthorizationFlowRequest request) {
        return paymentsApi.startAuthorizationFlow(getRequestScopes(), toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String paymentId, SubmitProviderSelectionRequest request) {
        return paymentsApi.submitProviderSelection(getRequestScopes(), emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            Headers headers, String paymentId, SubmitProviderSelectionRequest request) {
        return paymentsApi.submitProviderSelection(getRequestScopes(), toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(String paymentId) {
        return paymentsApi.submitConsent(getRequestScopes(), emptyMap(), paymentId, new EmptyRequestBody());
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitConsent(Headers headers, String paymentId) {
        return paymentsApi.submitConsent(getRequestScopes(), toMap(headers), paymentId, new EmptyRequestBody());
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(
            String paymentId, SubmitFormRequest request) {
        return paymentsApi.submitForm(getRequestScopes(), emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitForm(
            Headers headers, String paymentId, SubmitFormRequest request) {
        return paymentsApi.submitForm(getRequestScopes(), toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            String paymentId, CreatePaymentRefundRequest request) {
        return paymentsApi.createPaymentRefund(getRequestScopes(), emptyMap(), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreatePaymentRefundResponse>> createPaymentRefund(
            Headers headers, String paymentId, CreatePaymentRefundRequest request) {
        return paymentsApi.createPaymentRefund(getRequestScopes(), toMap(headers), paymentId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<ListPaymentRefundsResponse>> listPaymentRefunds(String paymentId) {
        return paymentsApi.listPaymentRefunds(getRequestScopes(), paymentId);
    }

    @Override
    public CompletableFuture<ApiResponse<PaymentRefund>> getPaymentRefundById(String paymentId, String refundId) {
        return paymentsApi.getPaymentRefundById(getRequestScopes(), paymentId, refundId);
    }
}
