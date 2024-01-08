package com.truelayer.java.commonapi;

import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnRequest;
import com.truelayer.java.commonapi.entities.SubmitPaymentsProviderReturnResponse;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * Exposes all the general purpose capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/submit-payments-provider-return-parameters">General purpose API reference</a>
 */
public interface ICommonApi {

    /**
     * Submits payments return parameters.
     * @param headers map representing custom HTTP headers to be sent
     * @param request a submit payment return parameters payload
     * @return the response of the <i>Submit payment returns parameters</i> operation
     * @see <a href="https://docs.truelayer.com/reference/submit-payments-provider-return-parameters"><i>Submit payments return parameters</i> API reference</a>
     */
    @POST("/payments-provider-return")
    CompletableFuture<ApiResponse<SubmitPaymentsProviderReturnResponse>> submitPaymentReturnParameters(
            @HeaderMap Map<String, String> headers, @Body SubmitPaymentsProviderReturnRequest request);
}
