package com.truelayer.java.mandates;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import com.truelayer.java.mandates.entities.ListMandatesQuery;
import com.truelayer.java.mandates.entities.ListMandatesResponse;
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
        return mandatesApi.createMandate(request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            String mandateId, StartAuthorizationFlowRequest request) {
        return mandatesApi.startAuthorizationFlow(mandateId, request);
    }

    @Override
    public CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            String mandateId, SubmitProviderSelectionRequest request) {
        return mandatesApi.submitProviderSelection(mandateId, request);
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
        return mandatesApi.revokeMandate(mandateId);
    }
}
