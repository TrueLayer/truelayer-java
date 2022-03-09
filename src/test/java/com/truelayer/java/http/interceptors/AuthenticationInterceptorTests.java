package com.truelayer.java.http.interceptors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import okhttp3.Interceptor;
import org.junit.jupiter.api.*;

class AuthenticationInterceptorTests extends BaseInterceptorTests {

    private AuthenticationInterceptor interceptor;

    @Override
    protected Interceptor getInterceptor() {
        return this.interceptor;
    }

    @BeforeEach
    public void prepareTest() {
        buildRequest();
    }

    @Test
    @DisplayName("It should add an authorization header to the original request")
    public void shouldAddAuthorizationHeader() {
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        List<String> scopes = Collections.singletonList("payments");
        ApiResponse<AccessToken> expectedAccessToken = TestUtils.buildAccessToken();
        when(authenticationHandler.getOauthToken(scopes))
                .thenReturn(CompletableFuture.completedFuture(expectedAccessToken));
        this.interceptor = new AuthenticationInterceptor(new AccessTokenManager(authenticationHandler, scopes));

        intercept();

        verifyThat(request -> assertEquals(
                "Bearer " + expectedAccessToken.getData().getAccessToken(),
                request.header(Constants.HeaderNames.AUTHORIZATION)));
    }

    @Test
    @DisplayName("It should throw an exception if authentication fails")
    public void shouldThrowException() {
        IAuthenticationHandler authenticationHandler = mock(AuthenticationHandler.class);
        List<String> scopes = Collections.singletonList("payments");
        when(authenticationHandler.getOauthToken(scopes))
                .thenReturn(CompletableFuture.completedFuture(ApiResponse.<AccessToken>builder()
                        .error(ProblemDetails.builder()
                                .type("error")
                                .detail("error: invalid_client")
                                .build())
                        .build()));
        this.interceptor = new AuthenticationInterceptor(new AccessTokenManager(authenticationHandler, scopes));

        Throwable thrown = Assertions.assertThrows(TrueLayerException.class, this::intercept);

        assertTrue(thrown.getMessage().startsWith("Unable to authenticate request"));
    }
}
