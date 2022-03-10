package com.truelayer.java.http.auth.cache;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.auth.entities.AccessToken;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * In memory cache
 */
public class SimpleAccessTokenCache implements IAccessTokenCache {

    /**
     * internal state
     */
    private AccessTokenRecord tokenRecord;

    private final Clock clock;

    public SimpleAccessTokenCache(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Optional<AccessToken> get() {
        if (isEmpty(tokenRecord) || LocalDateTime.now(clock).compareTo(tokenRecord.expiresAt) >= 0) {
            return Optional.empty();
        }

        return Optional.of(tokenRecord.token);
    }

    @Override
    public void store(AccessToken token) {
        tokenRecord = new AccessTokenRecord(token, LocalDateTime.now(clock).plusSeconds(token.getExpiresIn()));
    }

    @Override
    public void clear() {
        tokenRecord = null;
    }

    @Getter
    @RequiredArgsConstructor
    public static class AccessTokenRecord {
        private final AccessToken token;
        private final LocalDateTime expiresAt;
    }
}
