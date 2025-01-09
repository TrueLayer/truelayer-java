package com.truelayer.java.http.auth.cache;

import com.truelayer.java.entities.RequestScopes;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CredentialsCacheHelper {
    private static final String CACHE_KEY_PREFIX = "tl-auth-token";

    public static String buildKey(String clientId, RequestScopes requestScopes) {
        List<String> scopes = new ArrayList<>(requestScopes.getScopes());
        Collections.sort(scopes);
        return MessageFormat.format("{0}:{1}:{2}", CACHE_KEY_PREFIX, clientId, scopes.hashCode());
    }
}
