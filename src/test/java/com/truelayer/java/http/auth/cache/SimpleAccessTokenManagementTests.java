package com.truelayer.java.http.auth.cache;

import static com.truelayer.java.TestUtils.buildAccessToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.truelayer.java.Constants;
import com.truelayer.java.auth.entities.AccessToken;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import com.truelayer.java.entities.RequestScopes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SimpleAccessTokenManagementTests {

    static final RequestScopes scopes = new RequestScopes(Collections.singletonList(Constants.Scopes.PAYMENTS));

    @Test
    @DisplayName("It should store a token record")
    public void shouldStoreATokenRecord() {

        AccessToken expectedToken = buildAccessToken().getData();
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());

        sut.storeToken(scopes, expectedToken);

        assertEquals(expectedToken, sut.getToken(scopes).get());
    }

    @Test
    @DisplayName("It should clear a token record")
    public void itShouldClearTheExistingToken() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        sut.storeToken(scopes, buildAccessToken().getData());

        sut.clearToken();

        assertFalse(sut.getToken(scopes).isPresent());
    }

    @Test
    @DisplayName("It should yield an empty optional if there are no cached tokens")
    public void itShouldYieldAnEmptyOptionalIfNoToken() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());

        assertFalse(sut.getToken(scopes).isPresent());
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
        SimpleCredentialsCache sut = new SimpleCredentialsCache(fakeClock);
        sut.storeToken(scopes, accessToken);

        assertFalse(sut.getToken(scopes).isPresent());
    }

    @Test
    @DisplayName("It should yield an token if token is not expired")
    public void itShouldYieldAnToken() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        sut.storeToken(scopes, buildAccessToken().getData());

        assertTrue(sut.getToken(scopes).isPresent());
    }
}
