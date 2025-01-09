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
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InMemoryCredentialsCacheTests {

    static final RequestScopes scopes = RequestScopes.builder().scope(PAYMENTS).build();

    @Test
    @DisplayName("It should store a token record")
    public void shouldStoreATokenRecord() {
        AccessToken expectedToken = buildAccessToken().getData();
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        String clientId = UUID.randomUUID().toString();
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);

        sut.storeToken(cacheKey, expectedToken);

        assertEquals(expectedToken, sut.getToken(cacheKey).get());
    }

    @Test
    @DisplayName("It should clear a token record")
    public void itShouldClearTheExistingToken() {
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        AccessToken accessToken = buildAccessToken().getData();
        String clientId = UUID.randomUUID().toString();
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);

        sut.storeToken(cacheKey, accessToken);

        sut.clearToken(cacheKey);

        assertFalse(sut.getToken(cacheKey).isPresent());
    }

    @Test
    @DisplayName("It should yield an empty optional if there are no cached tokens")
    public void itShouldYieldAnEmptyOptionalIfNoToken() {
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        String clientId = UUID.randomUUID().toString();
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);

        assertFalse(sut.getToken(cacheKey).isPresent());
    }

    @Test
    @DisplayName("It should yield an empty optional if token is expired")
    public void itShouldYieldAnEmptyOptionalIfTokenExpired() {
        String clientId = UUID.randomUUID().toString();
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);
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
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(fakeClock);
        sut.storeToken(cacheKey, accessToken);

        assertFalse(sut.getToken(cacheKey).isPresent());
    }

    @Test
    @DisplayName("It should yield an token if token is not expired")
    public void itShouldYieldAnToken() {
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        String clientId = UUID.randomUUID().toString();
        String cacheKey = CredentialsCacheHelper.buildKey(clientId, scopes);

        sut.storeToken(cacheKey, buildAccessToken().getData());

        assertTrue(sut.getToken(cacheKey).isPresent());
    }

    @Test
    @DisplayName("It should yield different tokens with different scopes")
    public void itShouldYieldDifferentTokensWithDifferentScopes() {
        String clientId = UUID.randomUUID().toString();
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        RequestScopes paymentsScopes = RequestScopes.builder().scope(PAYMENTS).build();
        RequestScopes vrpScopes =
                RequestScopes.builder().scope(RECURRING_PAYMENTS_SWEEPING).build();

        String paymentsCacheKey = CredentialsCacheHelper.buildKey(clientId, paymentsScopes);
        String vrpCacheKey = CredentialsCacheHelper.buildKey(clientId, vrpScopes);

        AccessToken paymentsAccessToken = buildAccessToken().getData();
        AccessToken vrpAccessToken = buildAccessToken().getData();

        sut.storeToken(paymentsCacheKey, paymentsAccessToken);
        sut.storeToken(vrpCacheKey, vrpAccessToken);

        assertTrue(sut.getToken(paymentsCacheKey).isPresent());
        assertEquals(paymentsAccessToken, sut.getToken(paymentsCacheKey).get());
        assertTrue(sut.getToken(vrpCacheKey).isPresent());
        assertEquals(vrpAccessToken, sut.getToken(vrpCacheKey).get());
    }

    @Test
    @DisplayName("It should yield the token with same scopes in different order")
    public void itShouldYieldTokenWithSameScopesInDifferentOrder() {
        String clientId = UUID.randomUUID().toString();
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        RequestScopes storingScopes = RequestScopes.builder()
                .scopes(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING))
                .build();
        RequestScopes requestingScopes = RequestScopes.builder()
                .scopes(Arrays.asList(RECURRING_PAYMENTS_SWEEPING, PAYMENTS))
                .build();
        String storingScopesCacheKey = CredentialsCacheHelper.buildKey(clientId, storingScopes);
        String requestingScopesCacheKey = CredentialsCacheHelper.buildKey(clientId, requestingScopes);

        AccessToken accessToken = buildAccessToken().getData();

        sut.storeToken(storingScopesCacheKey, accessToken);

        assertTrue(sut.getToken(requestingScopesCacheKey).isPresent());
        assertEquals(accessToken, sut.getToken(requestingScopesCacheKey).get());
    }

    @Test
    @DisplayName("It should replace the token with same scopes in different order")
    public void itShouldReplaceTheTokenWithSameScopesInDifferentOrder() {
        String clientId = UUID.randomUUID().toString();
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        RequestScopes orderedStoringScopes = RequestScopes.builder()
                .scopes(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING))
                .build();
        RequestScopes reverseOrderedStoringScopes = RequestScopes.builder()
                .scopes(Arrays.asList(RECURRING_PAYMENTS_SWEEPING, PAYMENTS))
                .build();

        String orderedStoringScopesCacheKey = CredentialsCacheHelper.buildKey(clientId, orderedStoringScopes);
        String reverseOrderedStoringScopesCacheKey =
                CredentialsCacheHelper.buildKey(clientId, reverseOrderedStoringScopes);

        AccessToken anAccessToken = buildAccessToken().getData();
        AccessToken anotherAccessToken = buildAccessToken().getData();

        sut.storeToken(orderedStoringScopesCacheKey, anAccessToken);
        sut.storeToken(reverseOrderedStoringScopesCacheKey, anotherAccessToken);

        assertTrue(sut.getToken(orderedStoringScopesCacheKey).isPresent());
        assertEquals(
                anotherAccessToken, sut.getToken(orderedStoringScopesCacheKey).get());
    }

    @Test
    @DisplayName("It should clear the cache only for the required scopes")
    public void itShouldClearTheCacheOnlyForRequiredScopes() {
        String clientId = UUID.randomUUID().toString();
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        RequestScopes paymentsScopes = RequestScopes.builder().scope(PAYMENTS).build();
        RequestScopes vrpScopes =
                RequestScopes.builder().scope(RECURRING_PAYMENTS_SWEEPING).build();
        String paymentsCacheKey = CredentialsCacheHelper.buildKey(clientId, paymentsScopes);
        String vrpCacheKey = CredentialsCacheHelper.buildKey(clientId, vrpScopes);

        AccessToken paymentsAccessToken = buildAccessToken().getData();
        AccessToken vrpAccessToken = buildAccessToken().getData();

        sut.storeToken(paymentsCacheKey, paymentsAccessToken);
        sut.storeToken(vrpCacheKey, vrpAccessToken);

        sut.clearToken(paymentsCacheKey);

        assertFalse(sut.getToken(paymentsCacheKey).isPresent());
        assertTrue(sut.getToken(vrpCacheKey).isPresent());
    }

    @Test
    @DisplayName("It should clear the cache with same scopes in different order")
    public void itShouldClearTheCacheWithSameScopesInDifferentOrder() {
        String clientId = UUID.randomUUID().toString();
        InMemoryCredentialsCache sut = new InMemoryCredentialsCache(Clock.systemUTC());
        RequestScopes storingScopes = RequestScopes.builder()
                .scopes(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING))
                .build();
        RequestScopes clearCacheScopes = RequestScopes.builder()
                .scopes(Arrays.asList(RECURRING_PAYMENTS_SWEEPING, PAYMENTS))
                .build();

        String storingScopesCacheKey = CredentialsCacheHelper.buildKey(clientId, storingScopes);
        String clearingScopesCacheKey = CredentialsCacheHelper.buildKey(clientId, clearCacheScopes);

        AccessToken accessToken = buildAccessToken().getData();

        sut.storeToken(storingScopesCacheKey, accessToken);
        sut.clearToken(clearingScopesCacheKey);

        assertFalse(sut.getToken(storingScopesCacheKey).isPresent());
    }
}
