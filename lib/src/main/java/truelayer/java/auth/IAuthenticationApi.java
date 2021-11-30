package truelayer.java.auth;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.ApiResponse;
import truelayer.java.http.ApiResponseCallAdapter;
import truelayer.java.http.ApiResponseCallAdapter.ApiCall;

import java.util.List;

public interface IAuthenticationApi {

    @FormUrlEncoded
    @POST("/connect/token")
    ApiCall<ApiResponse<AccessToken>> getOauthToken(
        @Field("client_id") String clientId,
        @Field("client_secret") String clientSecret,
        @Field("grant_type") String grantType,
        @Field("scopes") List<String> scopes);
}
