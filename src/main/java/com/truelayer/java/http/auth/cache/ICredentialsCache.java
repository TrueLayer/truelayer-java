package com.truelayer.java.http.auth.cache;

import com.truelayer.java.auth.entities.AccessToken;
import java.util.Optional;

public interface ICredentialsCache {

    /**
     * Gets a cached access token.
     * @return an optional access token. If the token is expired an empty optional is returned
     */
    Optional<AccessToken> getToken();

    /**
     * Stores an access token in cache
     * @param token the new token to store
     */
    void storeToken(AccessToken token);

    /**
     * Resets the cache
     */
    void clearToken();
}
