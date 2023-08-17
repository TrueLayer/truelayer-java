package com.truelayer.java.mandates;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.Constants.Scopes.RECURRING_PAYMENTS_SWEEPING;
import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.IAuthenticatedHandler;
import com.truelayer.java.entities.RequestScopes;
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
public class MandatesHandler implements IAuthenticatedHandler, IMandatesHandler {
    IMandatesApi mandatesApi;

    @Override
    public RequestScopes getRequestScopes() {
        return RequestScopes.builder()
                .scope(PAYMENTS)
                .scope(RECURRING_PAYMENTS_SWEEPING)
                .build();
    }

    @Override
    public CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(CreateMandateRequest request) {
        return mandatesApi.createMandate(getRequestScopes(), emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(
            Headers headers, CreateMandateRequest request) {
        return mandatesApi.createMandate(getRequestScopes(), toMap(headers), request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String mandateId, StartAuthorizationFlowRequest request) {
        return mandatesApi.startAuthorizationFlow(getRequestScopes(), emptyMap(), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            Headers headers, String mandateId, StartAuthorizationFlowRequest request) {
        return mandatesApi.startAuthorizationFlow(getRequestScopes(), toMap(headers), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String mandateId, SubmitProviderSelectionRequest request) {
        return mandatesApi.submitProviderSelection(getRequestScopes(), emptyMap(), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            Headers headers, String mandateId, SubmitProviderSelectionRequest request) {
        return mandatesApi.submitProviderSelection(getRequestScopes(), toMap(headers), mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates() {
        return mandatesApi.listMandates(getRequestScopes(), null, null, null);
    }

    @Override
    public CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates(ListMandatesQuery query) {
        return mandatesApi.listMandates(getRequestScopes(), query.userId(), query.cursor(), query.limit());
    }

    @Override
    public CompletableFuture<ApiResponse<MandateDetail>> getMandate(String mandateId) {
        return mandatesApi.getMandate(getRequestScopes(), mandateId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> revokeMandate(String mandateId) {
        return mandatesApi.revokeMandate(getRequestScopes(), emptyMap(), mandateId);
    }

    @Override
    public CompletableFuture<ApiResponse<Void>> revokeMandate(Headers headers, String mandateId) {
        return mandatesApi.revokeMandate(getRequestScopes(), toMap(headers), mandateId);
    }

    @Override
    public CompletableFuture<ApiResponse<GetConfirmationOfFundsResponse>> getConfirmationOfFunds(
            String mandateId, String amount_in_minor, String currency) {
        return mandatesApi.getConfirmationOfFunds(getRequestScopes(), mandateId, amount_in_minor, currency);
    }

    @Override
    public CompletableFuture<ApiResponse<GetConstraintsResponse>> getMandateConstraints(String mandateId) {
        return mandatesApi.getMandateConstraints(getRequestScopes(), mandateId);
    }
}
