package com.truelayer.java.http.auth;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.cache.IAccessTokenCache;
import com.truelayer.java.http.auth.cache.SimpleAccessTokenCache;
import com.truelayer.java.http.entities.ApiResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static com.truelayer.java.TestUtils.buildAccessToken;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccessTokenManagerTests {

    @Test
    @DisplayName("It should get a cached token")
    public void itShouldGetACachedToken(){
        AccessToken expectedToken = buildAccessToken().getData();
        IAccessTokenCache cache = mock(SimpleAccessTokenCache.class);
        when(cache.get()).thenReturn(Optional.of(expectedToken));
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken();

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).get();
        verify(authenticationHandler, never()).getOauthToken(eq(singletonList(PAYMENTS)));
    }

    @Test
    @DisplayName("It should get a new token and store it in cache")
    public void itShouldGetAFreshToken(){
        AccessToken expectedToken = buildAccessToken().getData();
        IAccessTokenCache cache = mock(SimpleAccessTokenCache.class);
        when(cache.get()).thenReturn(Optional.empty());
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        when(authenticationHandler.getOauthToken(eq(singletonList(PAYMENTS)))).thenReturn(
                CompletableFuture.completedFuture(ApiResponse.<AccessToken>builder().data(expectedToken).build()));
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken();

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).get();
        verify(authenticationHandler, times(1)).getOauthToken(eq(singletonList(PAYMENTS)));
        verify(cache, times(1)).store(eq(expectedToken));
    }

    @Test
    @DisplayName("It should invalidate an existing token")
    public void itShouldInvalidateExistingToken(){
        IAccessTokenCache cache = mock(SimpleAccessTokenCache.class);
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        sut.invalidateToken();

        verify(cache, times(1)).clear();
    }
}
