package com.truelayer.java.payouts;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.payouts.entities.CreatePayoutRequest;
import com.truelayer.java.payouts.entities.CreatePayoutResponse;
import com.truelayer.java.payouts.entities.Payout;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

public interface IPayoutsApi {

    /**
     * Pay out from one of your merchant accounts.
     * @param headers map representing custom HTTP headers to be sent
     * @param request a create payout request payload
     * @return the response of the <i>Create Payout</i> operation
     * @see <a href="https://docs.truelayer.com/reference/create-payout"><i>Create Payout</i> API reference</a>
     */
    @POST("/payouts")
    CompletableFuture<ApiResponse<CreatePayoutResponse>> createPayout(
            @HeaderMap Map<String, String> headers, @Body CreatePayoutRequest request);

    /**
     * Returns payout details.
     * @param payoutId the payout identifier
     * @return the response of the <i>Get Payout</i> operation
     * @see <a href="https://docs.truelayer.com/reference/get-payoutt"><i>Get Payout</i> API reference</a>
     */
    @GET("/payouts/{id}")
    CompletableFuture<ApiResponse<Payout>> getPayout(@Path("id") String payoutId);
}
