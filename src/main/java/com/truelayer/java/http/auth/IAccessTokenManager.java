package com.truelayer.java.http.auth;

import com.truelayer.java.auth.entities.AccessToken;

/**
 * Manages an internal state for access tokens.
 */
public interface IAccessTokenManager {

    /**
     * Gets either a cached or a fresh token based on internal decisions.
     * @return the access token
     */
    AccessToken get();

    /**
     * Reset the state for access tokens. Usually called in case of 401 received by the API
     */
    void invalidate();
}
