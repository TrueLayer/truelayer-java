package com.truelayer.java.auth;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Interface that models /connect/* endpoints
 */
public interface IAuthenticationApi {

    @FormUrlEncoded
    @POST("/connect/token")
    CompletableFuture<ApiResponse<AccessToken>> getOauthTokenAsync(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("scopes") List<String> scopes);

    @FormUrlEncoded
    @POST("/connect/token")
    ApiResponse<AccessToken> getOauthToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("scopes") List<String> scopes);
}
