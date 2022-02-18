package truelayer.java.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static truelayer.java.TestUtils.buildAccessToken;
import static truelayer.java.TestUtils.getClientCredentials;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.ClientCredentials;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;

public class AuthenticationHandlerTests {

    public static final List<String> SCOPES = Collections.singletonList("a-scope");

    @SneakyThrows
    @Test
    @DisplayName("It should get an access token")
    public void shouldReturnABuilderInstance() {
        ClientCredentials clientCredentials = getClientCredentials();
        IAuthenticationApi authenticationApi = mock(IAuthenticationApi.class);
        ApiResponse<AccessToken> expectedToken = buildAccessToken();
        when(authenticationApi.getOauthToken(
                        clientCredentials.clientId(),
                        clientCredentials.clientSecret(),
                        ClientCredentials.GRANT_TYPE,
                        SCOPES))
                .thenReturn(CompletableFuture.completedFuture(expectedToken));
        AuthenticationHandler sut = new AuthenticationHandler(clientCredentials, authenticationApi);

        ApiResponse<AccessToken> token = sut.getOauthToken(SCOPES).get();

        assertEquals(expectedToken, token);
    }
}
