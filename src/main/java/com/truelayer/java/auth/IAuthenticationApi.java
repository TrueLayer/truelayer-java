package com.truelayer.java.auth;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.auth.entities.GenerateOauthTokenRequest;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Interface that models /connect/* endpoints
 */
public interface IAuthenticationApi {

    @POST("/connect/token")
    CompletableFuture<ApiResponse<AccessToken>> generateOauthToken(
            @Body GenerateOauthTokenRequest generateOauthTokenRequest);
}
