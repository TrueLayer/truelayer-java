package com.truelayer.java.recurringpayments;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.recurringpayments.entities.CreateMandateRequest;
import com.truelayer.java.recurringpayments.entities.CreateMandateResponse;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IMandatesApi {

    @POST("/mandates")
    CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(@Body CreateMandateRequest request);
}
