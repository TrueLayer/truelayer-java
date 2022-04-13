package com.truelayer.java.mandates;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IMandatesApi {

    @POST("/mandates")
    CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(@Body CreateMandateRequest request);

    @POST("/mandates/{id}/authorization-flow")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            @Path("id") String mandateId, @Body StartAuthorizationFlowRequest request);

    @POST("/mandates/{id}/authorization-flow/actions/provider-selection")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            @Path("id") String mandateId, @Body SubmitProviderSelectionRequest request);

    @GET("/mandates/{id}")
    CompletableFuture<ApiResponse<MandateDetail>> getMandate(@Path("id") String mandateId);

    @POST("/mandates/{id}/revoke")
    CompletableFuture<ApiResponse<Void>> revokeMandate(@Path("id") String mandateId);
}
