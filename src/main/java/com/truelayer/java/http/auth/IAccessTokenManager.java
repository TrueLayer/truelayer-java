package com.truelayer.java.http.auth;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;

/**
 * Manages an internal state for access tokens.
 */
public interface IAccessTokenManager {

    /**
     * Gets either a cached or a fresh token based on internal decisions.
     * @param scopes the requested scopes
     * @return the access token
     */
    AccessToken getToken(RequestScopes scopes);

    /**
     * Reset the state for access token related to requested scopes. Called in case of 401 received by the API
     * @param scopes the requested scopes
     */
    void invalidateToken(RequestScopes scopes);
}
