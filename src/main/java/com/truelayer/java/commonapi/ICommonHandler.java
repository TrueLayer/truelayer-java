package com.truelayer.java.commonapi;

import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import java.util.concurrent.CompletableFuture;

public interface ICommonHandler {

    CompletableFuture<ApiResponse<SubmitPaymentReturnParametersResponse>> submitPaymentReturnParameters(
            SubmitPaymentReturnParametersRequest request);

    CompletableFuture<ApiResponse<SubmitPaymentReturnParametersResponse>> submitPaymentReturnParameters(
            Headers headers, SubmitPaymentReturnParametersRequest request);
}
