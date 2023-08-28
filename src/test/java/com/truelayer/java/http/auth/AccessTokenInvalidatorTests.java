package com.truelayer.java.http.auth;

import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static com.truelayer.java.Constants.Scopes.PAYMENTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.truelayer.java.entities.RequestScopes;
import lombok.SneakyThrows;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AccessTokenInvalidatorTests {

    @SneakyThrows
    @Test
    @DisplayName("It should invalidate a token")
    public void itShouldInvalidateAToken() {
        AccessTokenManager tokenManager = mock(AccessTokenManager.class);
        AccessTokenInvalidator sut = new AccessTokenInvalidator(tokenManager);
        String accessToken = "a-token";
        RequestScopes requestScopes = RequestScopes.builder().scope(PAYMENTS).build();
        Response response = mock(Response.class);
        when(response.request())
                .thenReturn(new Request.Builder()
                        .url("http://localhost")
                        .get()
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .tag(RequestScopes.class, requestScopes)
                        .build());

        sut.authenticate(null, response);

        verify(tokenManager, times(1)).invalidateToken(requestScopes);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should not invalidate a token if absent on the request")
    public void itShouldNotInvalidateATokenIfNotSet() {
        AccessTokenManager tokenManager = mock(AccessTokenManager.class);
        AccessTokenInvalidator sut = new AccessTokenInvalidator(tokenManager);
        Response response = mock(Response.class);
        when(response.request())
                .thenReturn(new Request.Builder().url("http://localhost").get().build());

        sut.authenticate(null, response);

        verify(tokenManager, never()).invalidateToken(any(RequestScopes.class));
    }

    @SneakyThrows
    @Test
    @DisplayName("It should not invalidate a token if requested scopes are absent on the request")
    public void itShouldNotInvalidateATokenIfRequestedScopesNotSet() {
        AccessTokenManager tokenManager = mock(AccessTokenManager.class);
        AccessTokenInvalidator sut = new AccessTokenInvalidator(tokenManager);
        Response response = mock(Response.class);
        String accessToken = "a-token";
        when(response.request())
                .thenReturn(new Request.Builder()
                        .url("http://localhost")
                        .get()
                        .header(AUTHORIZATION, "Bearer " + accessToken)
                        .build());

        sut.authenticate(null, response);

        verify(tokenManager, never()).invalidateToken(any(RequestScopes.class));
    }
}
