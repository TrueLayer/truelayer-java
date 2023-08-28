package com.truelayer.java.http.interceptors;

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
import org.apache.commons.lang3.ObjectUtils;

@RequiredArgsConstructor
public class AuthenticationInterceptor implements Interceptor {

    private final IAccessTokenManager tokenManager;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!needsTokenGeneration(request)) {
            // we are here if a custom access token was specified on the request.
            // if so, we skip all the internal management
            return chain.proceed(request);
        }

        RequestScopes requestedScopes = request.tag(RequestScopes.class);
        if (ObjectUtils.isEmpty(requestedScopes) || requestedScopes.getScopes().isEmpty()) {
            // usually this means that we're not using the interceptors on authenticated calls
            throw new TrueLayerException("Missing request scopes tag on the outgoing request");
        }
        AccessToken accessToken = tokenManager.getToken(requestedScopes);

        Request newRequest = request.newBuilder()
                .header(Constants.HeaderNames.AUTHORIZATION, buildAuthorizationHeader(accessToken.getAccessToken()))
                .build();

        return chain.proceed(newRequest);
    }

    private boolean needsTokenGeneration(Request request) {
        return isEmpty(request.header(Constants.HeaderNames.AUTHORIZATION));
    }

    private String buildAuthorizationHeader(String token) {
        return "Bearer " + token;
    }
}
