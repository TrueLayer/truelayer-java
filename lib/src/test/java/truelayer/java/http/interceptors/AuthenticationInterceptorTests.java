package truelayer.java.http.interceptors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.TestUtils.buildAccessToken;

import java.util.List;
import okhttp3.Interceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;

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
        var authenticationHandler = mock(AuthenticationHandler.class);
        var scopes = List.of("payments");
        var expectedAccessToken = buildAccessToken();
        when(authenticationHandler.getOauthToken(scopes)).thenReturn(expectedAccessToken);
        this.interceptor = new AuthenticationInterceptor(authenticationHandler, scopes);

        intercept();

        verifyThat(request -> {
            assertEquals("Bearer " + expectedAccessToken.getData().getAccessToken(), request.header(AUTHORIZATION));
        });
    }

    @Test
    @DisplayName("It should throw an exception if authentication fails")
    public void shouldThrowException() {
        var authenticationHandler = mock(AuthenticationHandler.class);
        var scopes = List.of("payments");
        when(authenticationHandler.getOauthToken(scopes))
                .thenReturn(ApiResponse.<AccessToken>builder()
                        .error(ProblemDetails.builder()
                                .type("error")
                                .detail("error: invalid_client")
                                .build())
                        .build());
        this.interceptor = new AuthenticationInterceptor(authenticationHandler, scopes);

        var thrown = Assertions.assertThrows(TrueLayerException.class, () -> intercept());

        assertTrue(thrown.getMessage().startsWith("Unable to authenticate request"));
    }
}
