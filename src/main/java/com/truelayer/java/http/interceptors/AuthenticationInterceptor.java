package com.truelayer.java.http.interceptors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
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
        Request request = chain.request();

        RequestScopes requestedScopes = request.tag(RequestScopes.class);
        if (isEmpty(requestedScopes) || isEmpty(requestedScopes.getScopes())) {
            // usually this means that we're not using the interceptors on authenticated calls
            throw new TrueLayerException("Missing request scopes tag on the outgoing request");
        }
        AccessToken accessToken = tokenManager.getToken(requestedScopes);

        Request newRequest = request.newBuilder()
                .header(Constants.HeaderNames.AUTHORIZATION, buildAuthorizationHeader(accessToken.getAccessToken()))
                .build();

        return chain.proceed(newRequest);
    }

    private String buildAuthorizationHeader(String token) {
        return "Bearer " + token;
    }
}
