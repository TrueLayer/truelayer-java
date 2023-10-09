package com.truelayer.java.http.auth.cache;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import java.util.Optional;

public interface ICredentialsCache {

    /**
     * Gets the cached access token for the given request scopes.
     * @param scopes the requested scopes
     * @return an optional access token. If the token is expired an empty optional is returned
     */
    Optional<AccessToken> getToken(RequestScopes scopes);

    /**
     * Stores an access token in cache for the given request scopes.
     * @param token the new token to store
     * @param scopes the requested scopes
     */
    void storeToken(RequestScopes scopes, AccessToken token);

    /**
     * Remove the entry in the cache for the given request scopes.
     * @param scopes the requested scopes
     */
    void clearToken(RequestScopes scopes);
}
