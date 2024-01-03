package com.truelayer.java.commonapi;

import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnResponse;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import java.util.concurrent.CompletableFuture;

public interface ICommonHandler {

    CompletableFuture<ApiResponse<SubmitPaymentsProviderReturnResponse>> submitPaymentReturnParameters(
            SubmitPaymentsProviderReturnRequest request);

    CompletableFuture<ApiResponse<SubmitPaymentsProviderReturnResponse>> submitPaymentReturnParameters(
            Headers headers, SubmitPaymentsProviderReturnRequest request);
}
