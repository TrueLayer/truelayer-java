package com.truelayer.java.http.auth.cache;

import com.truelayer.java.auth.entities.AccessToken;
import java.util.Optional;

public interface ICredentialsCache {

    Optional<AccessToken> getToken(String key);

    void storeToken(String key, AccessToken token);

    void clearToken(String key);
}
