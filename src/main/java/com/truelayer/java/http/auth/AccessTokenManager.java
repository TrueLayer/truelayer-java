package com.truelayer.java.http.auth;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.entities.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import java.util.List;

@RequiredArgsConstructor
public class AccessTokenManager implements IAccessTokenManager {

    /**
     * Internal state
     */
    private AccessToken accessToken;

    private final IAuthenticationHandler authenticationHandler;

    private final List<String> scopes;

    @Override
    public AccessToken get() {
        this.accessToken = tryGetToken();

        //todo: cache

        return this.accessToken;
    }

    @Override
    @Synchronized
    public void invalidate() {
        this.accessToken = null;
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
