package com.truelayer.java.http.auth;

import static java.util.Collections.singletonList;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.cache.IAccessTokenCache;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.List;
import lombok.Builder;
import lombok.Synchronized;

@Builder
public class AccessTokenManager implements IAccessTokenManager {

    private final IAuthenticationHandler authenticationHandler;

    private final IAccessTokenCache accessTokenCache;

    private final List<String> scopes = singletonList(Constants.Scopes.PAYMENTS);

    @Override
    public AccessToken getToken() {
        return accessTokenCache.get().orElseGet(() -> {
            AccessToken token = tryGetToken();
            accessTokenCache.store(token);
            return token;
        });
    }

    @Override
    @Synchronized
    public void invalidateToken() {
        accessTokenCache.clear();
    }

    private AccessToken tryGetToken() {
        ApiResponse<AccessToken> accessTokenResponse;
        try {
            accessTokenResponse = authenticationHandler.getOauthToken(scopes).get();
        } catch (Exception e) {
            throw new TrueLayerException("unable to get an access token response", e);
        }

        if (accessTokenResponse.isError()) {
            throw new TrueLayerException(
                    String.format("Unable to authenticate request: %s", accessTokenResponse.getError()));
        }

        return accessTokenResponse.getData();
    }
}
