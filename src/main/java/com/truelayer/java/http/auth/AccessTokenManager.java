package com.truelayer.java.http.auth;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.auth.cache.CredentialsCacheHelper;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.Optional;
import lombok.Builder;
import lombok.Synchronized;

@Builder
public class AccessTokenManager implements IAccessTokenManager {

    private final String clientId;

    private final IAuthenticationHandler authenticationHandler;

    private final ICredentialsCache credentialsCache;

    private Optional<ICredentialsCache> getCredentialsCache() {
        return Optional.ofNullable(credentialsCache);
    }

    @Override
    public AccessToken getToken(RequestScopes scopes) {
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);

        if (getCredentialsCache().isPresent()) {
            return getCredentialsCache().get().getToken(cacheKey).orElseGet(() -> {
                AccessToken token = tryGetToken(scopes);
                credentialsCache.storeToken(cacheKey, token);
                return token;
            });
        }

        return tryGetToken(scopes);
    }

    @Override
    @Synchronized
    public void invalidateToken(RequestScopes scopes) {
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);
        getCredentialsCache().ifPresent(iCredentialsCache -> iCredentialsCache.clearToken(cacheKey));
    }

    private AccessToken tryGetToken(RequestScopes scopes) {
        ApiResponse<AccessToken> accessTokenResponse;
        try {
            accessTokenResponse =
                    authenticationHandler.getOauthToken(scopes.getScopes()).get();
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
