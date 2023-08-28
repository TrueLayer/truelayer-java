package com.truelayer.java.http.auth;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.Constants.Scopes.RECURRING_PAYMENTS_SWEEPING;
import static com.truelayer.java.TestUtils.buildAccessToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.auth.cache.SimpleCredentialsCache;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccessTokenManagerTests {

    @Test
    @DisplayName("It should get a cached token")
    public void itShouldGetACachedToken() {
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        RequestScopes scopes = RequestScopes.builder()
                .scope(PAYMENTS)
                .build();
        when(cache.getToken(scopes)).thenReturn(Optional.of(expectedToken));
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(scopes);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(scopes);
        verify(authenticationHandler, never()).getOauthToken(eq(scopes.getScopes()));
    }

    @Test
    @DisplayName("It should get a new token and store it in cache when requested scopes are not cached")
    public void itShouldGetAFreshTokenWhenRequestedScopesAreNotCached() {
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        RequestScopes scopes = RequestScopes.builder()
                .scope(PAYMENTS)
                .build();
        when(cache.getToken(scopes)).thenReturn(Optional.empty());
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        when(authenticationHandler.getOauthToken(eq(scopes.getScopes())))
                .thenReturn(CompletableFuture.completedFuture(
                        ApiResponse.<AccessToken>builder().data(expectedToken).build()));
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(scopes);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(scopes);
        verify(authenticationHandler, times(1)).getOauthToken(eq(scopes.getScopes()));
        verify(cache, times(1)).storeToken(eq(scopes), eq(expectedToken));
    }

    @Test
    @DisplayName("It should get a new token and store it in cache when different requested scopes are cached")
    public void itShouldGetAFreshTokenWhenDifferentRequestedScopesAreCached() {
        AccessToken expectedToken = buildAccessToken().getData();
        AccessToken storedPaymentsToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        RequestScopes paymentsScopes = RequestScopes.builder()
                .scope(PAYMENTS)
                .build();
        RequestScopes vrpScopes = RequestScopes.builder()
                .scope(RECURRING_PAYMENTS_SWEEPING)
                .build();
        when(cache.getToken(paymentsScopes)).thenReturn(Optional.of(storedPaymentsToken));
        when(cache.getToken(vrpScopes)).thenReturn(Optional.empty());
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        when(authenticationHandler.getOauthToken(eq(vrpScopes.getScopes())))
                .thenReturn(CompletableFuture.completedFuture(
                        ApiResponse.<AccessToken>builder().data(expectedToken).build()));
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(vrpScopes);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(vrpScopes);
        verify(authenticationHandler, times(1)).getOauthToken(eq(vrpScopes.getScopes()));
        verify(cache, times(1)).storeToken(eq(vrpScopes), eq(expectedToken));
    }

    @Test
    @DisplayName("It should invalidate an existing token")
    public void itShouldInvalidateExistingToken() {
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);
        RequestScopes scopes = RequestScopes.builder()
                .scope(PAYMENTS)
                .build();

        AccessToken accessToken = buildAccessToken().getData();
        cache.storeToken(scopes, accessToken);

        sut.invalidateToken(scopes);

        verify(cache, times(1)).clearToken(scopes);
    }
}
