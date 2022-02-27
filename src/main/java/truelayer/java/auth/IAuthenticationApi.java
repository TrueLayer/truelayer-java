package truelayer.java.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

/**
 * Interface that models /connect/* endpoints
 */
public interface IAuthenticationApi {

    @FormUrlEncoded
    @POST("/connect/token")
    CompletableFuture<ApiResponse<AccessToken>> getOauthToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("grant_type") String grantType,
            @Field("scopes") List<String> scopes);
}
