package com.truelayer.java.http.auth.cache;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.Constants.Scopes.RECURRING_PAYMENTS_SWEEPING;
import static com.truelayer.java.TestUtils.buildAccessToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class SimpleAccessTokenManagementTests {

    static final RequestScopes scopes = RequestScopes.builder().scope(PAYMENTS).build();

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
        AccessToken accessToken = buildAccessToken().getData();
        sut.storeToken(scopes, accessToken);

        sut.clearToken(scopes);

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

    @Test
    @DisplayName("It should yield different tokens with different scopes")
    public void itShouldYieldDifferentTokensWithDifferentScopes() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        RequestScopes paymentsScopes = RequestScopes.builder().scope(PAYMENTS).build();
        RequestScopes vrpScopes =
                RequestScopes.builder().scope(RECURRING_PAYMENTS_SWEEPING).build();

        AccessToken paymentsAccessToken = buildAccessToken().getData();
        AccessToken vrpAccessToken = buildAccessToken().getData();

        sut.storeToken(paymentsScopes, paymentsAccessToken);
        sut.storeToken(vrpScopes, vrpAccessToken);

        assertTrue(sut.getToken(paymentsScopes).isPresent());
        assertEquals(paymentsAccessToken, sut.getToken(paymentsScopes).get());
        assertTrue(sut.getToken(vrpScopes).isPresent());
        assertEquals(vrpAccessToken, sut.getToken(vrpScopes).get());
    }

    @Test
    @DisplayName("It should yield the token with same scopes in different order")
    public void itShouldYieldTokenWithSameScopesInDifferentOrder() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        RequestScopes storingScopes = RequestScopes.builder()
                .scopes(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING))
                .build();
        RequestScopes requestingScopes = RequestScopes.builder()
                .scopes(Arrays.asList(RECURRING_PAYMENTS_SWEEPING, PAYMENTS))
                .build();

        AccessToken accessToken = buildAccessToken().getData();

        sut.storeToken(storingScopes, accessToken);

        assertTrue(sut.getToken(requestingScopes).isPresent());
        assertEquals(accessToken, sut.getToken(requestingScopes).get());
    }

    @Test
    @DisplayName("It should replace the token with same scopes in different order")
    public void itShouldReplaceTheTokenWithSameScopesInDifferentOrder() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        RequestScopes orderedStoringScopes = RequestScopes.builder()
                .scopes(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING))
                .build();
        RequestScopes reverseOrderedStoringScopes = RequestScopes.builder()
                .scopes(Arrays.asList(RECURRING_PAYMENTS_SWEEPING, PAYMENTS))
                .build();

        AccessToken anAccessToken = buildAccessToken().getData();
        AccessToken anotherAccessToken = buildAccessToken().getData();

        sut.storeToken(orderedStoringScopes, anAccessToken);
        sut.storeToken(reverseOrderedStoringScopes, anotherAccessToken);

        assertTrue(sut.getToken(orderedStoringScopes).isPresent());
        assertEquals(anotherAccessToken, sut.getToken(orderedStoringScopes).get());
    }

    @Test
    @DisplayName("It should clear the cache only for the required scopes")
    public void itShouldClearTheCacheOnlyForRequiredScopes() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        RequestScopes paymentsScopes = RequestScopes.builder().scope(PAYMENTS).build();
        RequestScopes vrpScopes =
                RequestScopes.builder().scope(RECURRING_PAYMENTS_SWEEPING).build();

        AccessToken paymentsAccessToken = buildAccessToken().getData();
        AccessToken vrpAccessToken = buildAccessToken().getData();

        sut.storeToken(paymentsScopes, paymentsAccessToken);
        sut.storeToken(vrpScopes, vrpAccessToken);

        sut.clearToken(paymentsScopes);

        assertFalse(sut.getToken(paymentsScopes).isPresent());
        assertTrue(sut.getToken(vrpScopes).isPresent());
    }

    @Test
    @DisplayName("It should clear the cache with same scopes in different order")
    public void itShouldClearTheCacheWithSameScopesInDifferentOrder() {
        SimpleCredentialsCache sut = new SimpleCredentialsCache(Clock.systemUTC());
        RequestScopes storingScopes = RequestScopes.builder()
                .scopes(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING))
                .build();
        RequestScopes clearCacheScopes = RequestScopes.builder()
                .scopes(Arrays.asList(RECURRING_PAYMENTS_SWEEPING, PAYMENTS))
                .build();

        AccessToken accessToken = buildAccessToken().getData();

        sut.storeToken(storingScopes, accessToken);
        sut.clearToken(clearCacheScopes);

        assertFalse(sut.getToken(storingScopes).isPresent());
    }
}
