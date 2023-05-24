package com.truelayer.java.commonapi;

import static com.truelayer.java.http.mappers.HeadersMapper.toMap;
import static java.util.Collections.emptyMap;

import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentReturnParametersResponse;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.Headers;
import java.util.concurrent.CompletableFuture;
import lombok.Value;

@Value
public class CommonHandler implements ICommonHandler {

    ICommonApi commonApi;

    @Override
    public CompletableFuture<ApiResponse<SubmitPaymentReturnParametersResponse>> submitPaymentReturnParameters(
            SubmitPaymentReturnParametersRequest request) {
        return commonApi.submitPaymentReturnParameters(emptyMap(), request);
    }

    @Override
    public CompletableFuture<ApiResponse<SubmitPaymentReturnParametersResponse>> submitPaymentReturnParameters(
            Headers headers, SubmitPaymentReturnParametersRequest request) {
        return commonApi.submitPaymentReturnParameters(toMap(headers), request);
    }
}
