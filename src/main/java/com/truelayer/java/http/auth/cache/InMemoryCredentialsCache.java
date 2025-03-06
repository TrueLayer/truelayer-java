package com.truelayer.java.http.auth.cache;

import com.truelayer.java.auth.entities.AccessToken;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InMemoryCredentialsCache implements ICredentialsCache {

    private final Clock clock;

    /**
     * internal state
     */
    private final Map<String, AccessTokenRecord> tokenRecords = new ConcurrentHashMap<>();

    @Override
    public Optional<AccessToken> getToken(String key) {
        AccessTokenRecord tokenRecord = tokenRecords.get(key);
        if (tokenRecord == null || !LocalDateTime.now(clock).isBefore(tokenRecord.expiresAt)) {
            return Optional.empty();
        }

        return Optional.of(tokenRecord.token);
    }

    @Override
    public void storeToken(String key, AccessToken token) {
        AccessTokenRecord tokenRecord =
                new AccessTokenRecord(token, LocalDateTime.now(clock).plusSeconds(token.getExpiresIn()));

        tokenRecords.put(key, tokenRecord);
    }

    @Override
    public void clearToken(String key) {
        tokenRecords.remove(key);
    }

    @RequiredArgsConstructor
    public static class AccessTokenRecord {
        private final AccessToken token;
        private final LocalDateTime expiresAt;
    }
}
