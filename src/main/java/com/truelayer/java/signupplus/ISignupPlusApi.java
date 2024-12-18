package com.truelayer.java.signupplus;

import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.signupplus.entities.GenerateAuthUriRequest;
import com.truelayer.java.signupplus.entities.GenerateAuthUriResponse;
import com.truelayer.java.signupplus.entities.UserData;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

public interface ISignupPlusApi {

    /**
     * Get user data from a payment.
     * @param scopes the scopes to be used by the underlying Oauth token
     * @param paymentId the id of the payment
     * @return the identity data associated to the remitter of the payment
     * @see <a href="https://docs.truelayer.com/reference/getuserdatabypayment"><i>Get user data by payment</i> API reference</a>
     */
    @GET("/signup-plus/payments")
    CompletableFuture<ApiResponse<UserData>> getUserDataByPayment(
            @Tag RequestScopes scopes, @Query("payment_id") String paymentId);

    @POST("/signup-plus/authuri")
    CompletableFuture<ApiResponse<GenerateAuthUriResponse>> generateAuthUri(
            @Tag RequestScopes scopes, @Body GenerateAuthUriRequest request);
}
