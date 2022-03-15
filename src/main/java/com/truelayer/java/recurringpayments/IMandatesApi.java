package com.truelayer.java.recurringpayments;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.StartAuthorizationFlowResponse;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionResponse;
import com.truelayer.java.recurringpayments.entities.CreateMandateRequest;
import com.truelayer.java.recurringpayments.entities.CreateMandateResponse;
import com.truelayer.java.recurringpayments.entities.mandatedetail.MandateDetail;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IMandatesApi {

    @POST("/mandates")
    CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(@Body CreateMandateRequest request);

    @POST("/mandates/{id}/authorization-flow")
    CompletableFuture<ApiResponse<StartAuthorizationFlowResponse>> startAuthorizationFlow(
            @Path("id") String mandateId, @Body StartAuthorizationFlowRequest request);

    @POST("/mandates/{id}/authorization-flow/actions/provider-selection")
    CompletableFuture<ApiResponse<SubmitProviderSelectionResponse>> submitProviderSelection(
            @Path("id") String mandateId, @Body SubmitProviderSelectionRequest request);

    @GET("/mandates/{id}")
    CompletableFuture<ApiResponse<MandateDetail>> getMandate(@Path("id") String mandateId);
}
