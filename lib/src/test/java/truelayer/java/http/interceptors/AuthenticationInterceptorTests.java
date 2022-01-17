package truelayer.java.http.interceptors;

import lombok.SneakyThrows;
import okhttp3.Interceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.TestUtils.buildAccessToken;


class AuthenticationInterceptorTests extends BaseInterceptorTests {

    private final ApiResponse<AccessToken> A_TEST_ACCESS_TOKEN = buildAccessToken();

    @Override
    protected Interceptor getInterceptor() {
        var authenticationHandler = mock(AuthenticationHandler.class);
        var scopes = List.of("payments");
        when(authenticationHandler.getOauthToken(scopes)).thenReturn(A_TEST_ACCESS_TOKEN);

        return new AuthenticationInterceptor(authenticationHandler, scopes);
    }

    @BeforeEach
    public void prepareTest() {
        buildRequest();
    }

    @Test
    @SneakyThrows
    @DisplayName("It should add an authorization header to the original request")
    public void shouldAddAuthorizationHeader() {
        intercept();

        verifyThat(request -> {
            assertEquals("Bearer " + A_TEST_ACCESS_TOKEN.getData().getAccessToken(), request.header(AUTHORIZATION));
        });
    }
}