package com.truelayer.java.http.auth;

import static java.util.Collections.singletonList;

import com.truelayer.java.Constants;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Synchronized;

@Builder
public class AccessTokenManager implements IAccessTokenManager {

    private final IAuthenticationHandler authenticationHandler;

    private final ICredentialsCache credentialsCache;

    private final List<String> scopes = singletonList(Constants.Scopes.PAYMENTS);

    private Optional<ICredentialsCache> getCredentialsCache() {
        return Optional.ofNullable(credentialsCache);
    }

    @Override
    public AccessToken getToken() {
        getCredentialsCache()
                .ifPresent(credentialsCache -> credentialsCache.get().orElseGet(() -> {
                    AccessToken token = tryGetToken();
                    credentialsCache.store(token);
                    return token;
                }));

        return tryGetToken();
    }

    @Override
    @Synchronized
    public void invalidateToken() {
        getCredentialsCache().ifPresent(ICredentialsCache::clear);
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
