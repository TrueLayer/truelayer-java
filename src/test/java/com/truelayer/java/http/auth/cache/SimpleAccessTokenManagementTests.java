package com.truelayer.java.http.auth.cache;

import static com.truelayer.java.TestUtils.buildAccessToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.truelayer.java.auth.entities.AccessToken;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SimpleAccessTokenManagementTests {

    @Test
    @DisplayName("It should store a token record")
    public void shouldStoreATokenRecord() {
        AccessToken expectedToken = buildAccessToken().getData();
        SimpleAccessTokenCache sut = new SimpleAccessTokenCache(Clock.systemUTC());

        sut.store(expectedToken);

        assertEquals(expectedToken, sut.get().get());
    }

    @Test
    @DisplayName("It should clear a token record")
    public void itShouldClearTheExistingToken() {
        SimpleAccessTokenCache sut = new SimpleAccessTokenCache(Clock.systemUTC());
        sut.store(buildAccessToken().getData());

        sut.clear();

        assertFalse(sut.get().isPresent());
    }

    @Test
    @DisplayName("It should yield an empty optional if there are no cached tokens")
    public void itShouldYieldAnEmptyOptionalIfNoToken() {
        SimpleAccessTokenCache sut = new SimpleAccessTokenCache(Clock.systemUTC());

        assertFalse(sut.get().isPresent());
    }

    @Test
    @DisplayName("It should yield an empty optional if token is expired")
    public void itShouldYieldAnEmptyOptionalIfTokenExpired() {
        AccessToken accessToken = buildAccessToken().getData();
        // now() will return an instant which is behind the current time of (accessToken.expiresIn + 5 seconds)
        Instant aPastInstant = Clock.systemUTC().instant().minus(accessToken.getExpiresIn() + 5, ChronoUnit.SECONDS);
        Clock fakeClock = Mockito.mock(Clock.class);
        // when storing a token the clock will compute a time in the past.
        // Upon getting the token it will return the current timestamp.
        when(fakeClock.instant())
                .thenReturn(aPastInstant)
                .thenReturn(Clock.systemUTC().instant());
        when(fakeClock.getZone()).thenReturn(ZoneOffset.UTC);
        SimpleAccessTokenCache sut = new SimpleAccessTokenCache(fakeClock);
        sut.store(accessToken);

        assertFalse(sut.get().isPresent());
    }

    @Test
    @DisplayName("It should yield an token if token is not expired")
    public void itShouldYieldAnToken() {
        SimpleAccessTokenCache sut = new SimpleAccessTokenCache(Clock.systemUTC());
        sut.store(buildAccessToken().getData());

        assertTrue(sut.get().isPresent());
    }
}
