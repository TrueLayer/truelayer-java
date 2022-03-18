package com.truelayer.java.http.auth.cache;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.auth.entities.AccessToken;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Default in-memory cache implementation.
 * Class constructor accepts a Clock instance for improved testing purposes. Please note that this is not a monotonic implementation.
 * @see <a href="https://www.thoughtworks.com/insights/blog/test-driven-development-best-thing-has-happened-software-design">this article</a> for more context,
 */
public class SimpleCredentialsCache implements ICredentialsCache {

    /**
     * internal state
     */
    private AccessTokenRecord tokenRecord;

    private final Clock clock;

    /**
     * Constructor for this class.
     * @param clock clock instance
     */
    public SimpleCredentialsCache(Clock clock) {
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
