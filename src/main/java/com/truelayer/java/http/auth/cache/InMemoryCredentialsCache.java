package com.truelayer.java.http.auth.cache;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import java.text.MessageFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Default in-memory cache implementation.
 * Class constructor accepts a Clock instance for improved testing purposes. Please note that this is not a monotonic implementation.
 * @see <a href="https://www.thoughtworks.com/insights/blog/test-driven-development-best-thing-has-happened-software-design">this article</a> for more context,
 */
public class InMemoryCredentialsCache implements ICredentialsCache {

    /**
     * internal state
     */
    private final Map<String, AccessTokenRecord> tokenRecords = new HashMap<>();

    private final Clock clock;

    private final String clientId;

    public static final String CACHE_KEY_PREFIX = "tl-auth-token";

    /**
     * Constructor for this class.
     * @param clock clock instance
     */
    public InMemoryCredentialsCache(String clientId, Clock clock) {
        this.clientId = clientId;
        this.clock = clock;
    }

    @Override
    public Optional<AccessToken> getToken(RequestScopes scopes) {
        String key = computeCacheKey(scopes);

        AccessTokenRecord tokenRecord = tokenRecords.get(key);
        if (tokenRecord == null || !LocalDateTime.now(clock).isBefore(tokenRecord.expiresAt)) {
            return Optional.empty();
        }

        return Optional.of(tokenRecord.token);
    }

    @Override
    public void storeToken(RequestScopes scopes, AccessToken token) {
        AccessTokenRecord tokenRecord =
                new AccessTokenRecord(token, LocalDateTime.now(clock).plusSeconds(token.getExpiresIn()));

        tokenRecords.put(computeCacheKey(scopes), tokenRecord);
    }

    @Override
    public void clearToken(RequestScopes scopes) {
        tokenRecords.remove(computeCacheKey(scopes));
    }

    @Getter
    @RequiredArgsConstructor
    public static class AccessTokenRecord {
        private final AccessToken token;
        private final LocalDateTime expiresAt;
    }

    private String computeCacheKey(RequestScopes scopes) {
        List<String> sortedScopes = new ArrayList<>(scopes.getScopes());
        Collections.sort(sortedScopes);
        return MessageFormat.format("{0}:{1}:{2}", CACHE_KEY_PREFIX, this.clientId, sortedScopes.hashCode());
    }
}
