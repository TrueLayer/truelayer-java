package com.truelayer.java.http.interceptors;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.entities.ApiResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAuthenticationHandler authenticationHandler;

    private final List<String> scopes;

    @Override
    public Response intercept(Chain chain) throws IOException {
        AccessToken accessToken = tryGetToken(authenticationHandler.getOauthToken(scopes));

        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header(Constants.HeaderNames.AUTHORIZATION, buildAuthorizationHeader(accessToken.getAccessToken()))
                .build();
        return chain.proceed(newRequest);
    }

    private AccessToken tryGetToken(CompletableFuture<ApiResponse<AccessToken>> responseFuture) {
        ApiResponse<AccessToken> accessTokenResponse;
        try {
            accessTokenResponse = responseFuture.get();
        } catch (Exception e) {
            throw new TrueLayerException("unable to get an access token response", e);
        }

        if (accessTokenResponse.isError()) {
            throw new TrueLayerException(
                    String.format("Unable to authenticate request: %s", accessTokenResponse.getError()));
        }

        return accessTokenResponse.getData();
    }

    private String buildAuthorizationHeader(String token) {
        return "Bearer " + token;
    }
}
