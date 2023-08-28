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

    // FIXME: temp
    private static final RequestScopes SCOPES = RequestScopes.builder()
            .scope(PAYMENTS)
            .scope(RECURRING_PAYMENTS_SWEEPING)
            .build();

    @Test
    @DisplayName("It should get a cached token")
    public void itShouldGetACachedToken() {
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        when(cache.getToken(SCOPES)).thenReturn(Optional.of(expectedToken));
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(SCOPES);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(SCOPES);
        verify(authenticationHandler, never()).getOauthToken(eq(SCOPES.getScopes()));
    }

    @Test
    @DisplayName("It should get a new token and store it in cache")
    public void itShouldGetAFreshToken() {
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        when(cache.getToken(SCOPES)).thenReturn(Optional.empty());
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        when(authenticationHandler.getOauthToken(eq(SCOPES.getScopes())))
                .thenReturn(CompletableFuture.completedFuture(
                        ApiResponse.<AccessToken>builder().data(expectedToken).build()));
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(SCOPES);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(SCOPES);
        verify(authenticationHandler, times(1)).getOauthToken(eq(SCOPES.getScopes()));
        verify(cache, times(1)).storeToken(eq(SCOPES), eq(expectedToken));
    }

    @Test
    @DisplayName("It should invalidate an existing token")
    public void itShouldInvalidateExistingToken() {
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken accessToken = buildAccessToken().getData();
        cache.storeToken(SCOPES, accessToken);

        sut.invalidateToken(SCOPES);

        verify(cache, times(1)).clearToken(SCOPES);
    }
}
