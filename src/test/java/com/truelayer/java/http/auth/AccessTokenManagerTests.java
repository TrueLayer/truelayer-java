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
    private final List<String> REQUESTED_SCOPES =
            Collections.unmodifiableList(Arrays.asList(PAYMENTS, RECURRING_PAYMENTS_SWEEPING));

    @Test
    @DisplayName("It should get a cached token")
    public void itShouldGetACachedToken() {
        RequestScopes scopes = new RequestScopes(REQUESTED_SCOPES);
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        when(cache.getToken(scopes)).thenReturn(Optional.of(expectedToken));
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(scopes);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(scopes);
        verify(authenticationHandler, never()).getOauthToken(eq(REQUESTED_SCOPES));
    }

    @Test
    @DisplayName("It should get a new token and store it in cache")
    public void itShouldGetAFreshToken() {
        RequestScopes scopes = new RequestScopes(REQUESTED_SCOPES);
        AccessToken expectedToken = buildAccessToken().getData();
        ICredentialsCache cache = mock(SimpleCredentialsCache.class);
        when(cache.getToken(scopes)).thenReturn(Optional.empty());
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        when(authenticationHandler.getOauthToken(eq(REQUESTED_SCOPES)))
                .thenReturn(CompletableFuture.completedFuture(
                        ApiResponse.<AccessToken>builder().data(expectedToken).build()));
        AccessTokenManager sut = new AccessTokenManager(authenticationHandler, cache);

        AccessToken actualToken = sut.getToken(scopes);

        assertEquals(expectedToken, actualToken);
        verify(cache, times(1)).getToken(scopes);
        verify(authenticationHandler, times(1)).getOauthToken(eq(REQUESTED_SCOPES));
        verify(cache, times(1)).storeToken(eq(scopes), eq(expectedToken));
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
