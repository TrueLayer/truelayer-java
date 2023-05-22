package com.truelayer.java.mandates;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.mandates.entities.*;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import java.util.concurrent.CompletableFuture;

/**
 * Provides /mandates API integration without the burden of Retrofit's annotation
 * and improve both usability and backward compatibility for the implemented endpoints.
 */
public interface IMandatesHandler {
    CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(CreateMandateRequest request);

    CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(Headers headers, CreateMandateRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String mandateId, StartAuthorizationFlowRequest request);

    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String mandateId, SubmitProviderSelectionRequest request);

    CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates();

    CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates(ListMandatesQuery query);

    CompletableFuture<ApiResponse<MandateDetail>> getMandate(String mandateId);

    CompletableFuture<ApiResponse<Void>> revokeMandate(String mandateId);

    CompletableFuture<ApiResponse<GetConfirmationOfFundsResponse>> getConfirmationOfFunds(
            String mandateId, String amount, String currency);

    CompletableFuture<ApiResponse<GetConstraintsResponse>> getMandateConstraints(String mandateId);
}
