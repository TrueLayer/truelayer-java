package com.truelayer.java.http.auth;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;

import java.util.List;

/**
 * Manages an internal state for access tokens.
 */
public interface IAccessTokenManager {

    /**
     * Gets either a cached or a fresh token based on internal decisions.
     * @return the access token
     */
    AccessToken getToken(RequestScopes scopes);

    /**
     * Reset the state for access tokens. Called in case of 401 received by the API
     */
    void invalidateToken();
}
