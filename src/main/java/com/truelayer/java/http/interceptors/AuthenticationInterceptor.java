package com.truelayer.java.http.interceptors;

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
import org.apache.commons.lang3.ObjectUtils;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAccessTokenManager tokenManager;

    @Override
    public Response intercept(Chain chain) throws IOException {
        RequestScopes requestedScopes = chain.request().tag(RequestScopes.class);

        if (ObjectUtils.isEmpty(requestedScopes) || requestedScopes.getScopes().isEmpty()) {
            throw new TrueLayerException("Missing request scopes tag on the outgoing request");
        }

        AccessToken accessToken = tokenManager.getToken(requestedScopes);

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
