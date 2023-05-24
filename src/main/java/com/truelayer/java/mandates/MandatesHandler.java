package com.truelayer.java.mandates;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.mandates.entities.*;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

@Value
public class MandatesHandler implements IMandatesHandler {
    IMandatesApi mandatesApi;

    @Override
    public CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(CreateMandateRequest request) {
        return mandatesApi.createMandate(emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(
            Headers headers, CreateMandateRequest request) {
        return mandatesApi.createMandate(toMap(headers), request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String mandateId, StartAuthorizationFlowRequest request) {
        return mandatesApi.startAuthorizationFlow(emptyMap(), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            Headers headers, String mandateId, StartAuthorizationFlowRequest request) {
        return mandatesApi.startAuthorizationFlow(toMap(headers), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String mandateId, SubmitProviderSelectionRequest request) {
        return mandatesApi.submitProviderSelection(emptyMap(), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            Headers headers, String mandateId, SubmitProviderSelectionRequest request) {
        return mandatesApi.submitProviderSelection(toMap(headers), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates() {
        return mandatesApi.listMandates(null, null, null);
    }

    @Override
    public CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates(ListMandatesQuery query) {
        return mandatesApi.listMandates(query.userId(), query.cursor(), query.limit());
    }

    @Override
    public CompletableFuture<ApiResponse<MandateDetail>> getMandate(String mandateId) {
        return mandatesApi.getMandate(mandateId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> revokeMandate(String mandateId) {
        return mandatesApi.revokeMandate(emptyMap(), mandateId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> revokeMandate(Headers headers, String mandateId) {
        return mandatesApi.revokeMandate(toMap(headers), mandateId);
    }

    @Override
    public CompletableFuture<ApiResponse<GetConfirmationOfFundsResponse>> getConfirmationOfFunds(
            String mandateId, String amount_in_minor, String currency) {
        return mandatesApi.getConfirmationOfFunds(mandateId, amount_in_minor, currency);
    }

    @Override
    public CompletableFuture<ApiResponse<GetConstraintsResponse>> getMandateConstraints(String mandateId) {
        return mandatesApi.getMandateConstraints(mandateId);
    }
}
