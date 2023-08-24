package com.truelayer.java.http.auth;

import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import lombok.RequiredArgsConstructor;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

@RequiredArgsConstructor
public class AccessTokenInvalidator implements Authenticator {

    private final IAccessTokenManager tokenManager;

    @Override
    public Request authenticate(Route route, Response response) {
        if (isNotEmpty(response.request().header(AUTHORIZATION))) {
            // we are here in case of 401 on requests containing an Authorization HTTP header
            String accessToken = response.request().header(AUTHORIZATION);
            tokenManager.invalidateToken(accessToken.replace("Bearer", "").trim());
        }
        return null;
    }
}
