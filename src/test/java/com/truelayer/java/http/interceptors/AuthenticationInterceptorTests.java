package com.truelayer.java.http.interceptors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.auth.cache.InMemoryCredentialsCache;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import java.time.Clock;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AuthenticationInterceptorTests extends BaseInterceptorTests {

    private AuthenticationInterceptor interceptor;

    @Override
    protected Interceptor getInterceptor() {
        return this.interceptor;
    }

    @ParameterizedTest(name = "scopes={0}")
    @MethodSource("provideInvalidScopesTag")
    @DisplayName("It should throw an exception if request scopes are")
    public void shouldThrowExceptionIfRequestScopesMissing(RequestScopes scopes) {
        Request request = new Request.Builder()
                .url("http://localhost/payment")
                .method("POST", EMPTY_REQUEST_BODY)
                .build();
        arrangeRequest(request);
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        AccessTokenManager accessTokenManager = AccessTokenManager.builder()
                .credentialsCache(new InMemoryCredentialsCache(Clock.systemUTC()))
                .authenticationHandler(authenticationHandler)
                .build();

        this.interceptor = new AuthenticationInterceptor(accessTokenManager);

        Throwable thrown = Assertions.assertThrows(TrueLayerException.class, this::intercept);

        assertTrue(thrown.getMessage().startsWith("Missing request scopes tag on the outgoing request"));
    }

    @Test
    @DisplayName("It should throw an exception if authentication fails")
    public void shouldThrowExceptionIfAuthenticationFails() {
        arrangeRequest();
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);

        when(authenticationHandler.getOauthToken(SCOPES.getScopes()))
                .thenReturn(CompletableFuture.completedFuture(ApiResponse.<AccessToken>builder()
                        .error(ProblemDetails.builder()
                                .type("error")
                                .detail("invalid_client")
                                .build())
                        .build()));

        AccessTokenManager accessTokenManager = AccessTokenManager.builder()
                .credentialsCache(new InMemoryCredentialsCache(Clock.systemUTC()))
                .authenticationHandler(authenticationHandler)
                .build();

        this.interceptor = new AuthenticationInterceptor(accessTokenManager);

        Throwable thrown = Assertions.assertThrows(TrueLayerException.class, this::intercept);

        assertTrue(thrown.getMessage().startsWith("Unable to authenticate request"));
    }

    @Test
    @DisplayName("It should add an authorization header to the original request")
    public void shouldAddAuthorizationHeader() {
        arrangeRequest();
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);

        ApiResponse<AccessToken> expectedAccessToken = TestUtils.buildAccessToken();
        when(authenticationHandler.getOauthToken(SCOPES.getScopes()))
                .thenReturn(CompletableFuture.completedFuture(expectedAccessToken));

        AccessTokenManager accessTokenManager = AccessTokenManager.builder()
                .credentialsCache(new InMemoryCredentialsCache(Clock.systemUTC()))
                .authenticationHandler(authenticationHandler)
                .build();

        this.interceptor = new AuthenticationInterceptor(accessTokenManager);

        intercept();

        verify(authenticationHandler, times(1)).getOauthToken(SCOPES.getScopes());
        verifyThat(request -> assertEquals(
                "Bearer " + expectedAccessToken.getData().getAccessToken(),
                request.header(Constants.HeaderNames.AUTHORIZATION)));
    }

    public static Stream<Arguments> provideInvalidScopesTag() {
        return Stream.of(null, Arguments.of(RequestScopes.builder().build()));
    }
}
