package com.truelayer.java.http.interceptors;

import com.truelayer.java.Constants;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.auth.IAccessTokenManager;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAccessTokenManager tokenManager;

    @Override
    public Response intercept(Chain chain) throws IOException {
        RequestScopes scopes = chain.request().tag(RequestScopes.class);
        if(scopes == null || scopes.getScopes().isEmpty())
        {
            // default to payments scope if not explicitly set in the request
            scopes = new RequestScopes(Collections.singletonList(Constants.Scopes.PAYMENTS));
        }

        AccessToken accessToken = tokenManager.getToken(scopes);

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
