package com.truelayer.java.http.auth;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.buildAccessToken;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.auth.cache.SimpleCredentialsCache;
import com.truelayer.java.http.entities.ApiResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccessTokenManagerTests {

    @Test
    @DisplayName("It should get a cached token")
    public void itShouldGetACachedToken() {
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        when(cache.getToken()).thenReturn(Optional.of(expectedToken));
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken();

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken();
        // verify(authenticationHandler, never()).getOauthTokenAsync(eq(singletonList(PAYMENTS)));
        verify(authenticationHandler, never()).getOauthToken(eq(singletonList(PAYMENTS)));
    }

    @Test
    @DisplayName("It should get a new token and store it in cache")
    public void itShouldGetAFreshToken() {
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        when(cache.getToken()).thenReturn(Optional.empty());
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        /*when(authenticationHandler.getOauthTokenAsync(eq(singletonList(PAYMENTS))))
        .thenReturn(CompletableFuture.completedFuture(
                ApiResponse.<AccessToken>builder().data(expectedToken).build()));*/
        when(authenticationHandler.getOauthToken(eq(singletonList(PAYMENTS))))
                .thenReturn(
                        ApiResponse.<AccessToken>builder().data(expectedToken).build());
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken();

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken();
        // verify(authenticationHandler, times(1)).getOauthTokenAsync(eq(singletonList(PAYMENTS)));
        verify(authenticationHandler, times(1)).getOauthToken(eq(singletonList(PAYMENTS)));
        verify(cache, times(1)).storeToken(eq(expectedToken));
    }

    @Test
    @DisplayName("It should invalidate an existing token")
    public void itShouldInvalidateExistingToken() {
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        sut.invalidateToken();

        verify(cache, times(1)).clearToken();
    }
}
