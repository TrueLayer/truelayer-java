package com.truelayer.java.http.interceptors;

import com.truelayer.java.Constants;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.IAccessTokenManager;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAccessTokenManager tokenManager;

    @Override
    public Response intercept(Chain chain) throws IOException {
        AccessToken accessToken = tokenManager.getToken();

        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header(Constants.HeaderNames.AUTHORIZATION, buildAuthorizationHeader(accessToken.getAccessToken()))
                .build();

        return chain.proceed(newRequest);
    }

    private String buildAuthorizationHeader(String token) {
        return "Bearer " + token;
    }
}
