package truelayer.java.auth;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.io.IOException;
import java.util.UUID;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static retrofit2.Response.error;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.getClientCredentialsOptions;
import static truelayer.java.TestUtils.stubApiResponse;

public class AuthenticationHandlerTests {

    public static final String A_SCOPE = "paydirect";

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken() throws IOException, AuthenticationException {
        var token = AccessToken.builder()
                .accessToken(UUID.randomUUID().toString())
                .expiresIn(RandomUtils.nextInt()) //todo possibly remove apache commons?
                .tokenType(UUID.randomUUID().toString())
                .build();
        AuthenticationHandler authentication = new AuthenticationHandler(
                (clientId, clientSecret, grantType, scopes) ->
                        stubApiResponse(success(token)),
                getClientCredentialsOptions()
        );

        var oauthToken = authentication.getOauthToken(of(A_SCOPE));

        assertEquals(token, oauthToken);
    }

    @Test
    @DisplayName("It should throw an authentication exception if a 400 is returned by the token endpoint")
    public void itShouldThrowAnAuthenticationException() {
        AuthenticationHandler authentication = new AuthenticationHandler(
                (clientId, clientSecret, grantType, scopes) ->
                        stubApiResponse(error(400, ResponseBody.create("{\"error\": \"invalid_client\"}", MediaType.parse("application/json")))),
                getClientCredentialsOptions()
        );

        var exception = assertThrows(AuthenticationException.class, () -> {
            authentication.getOauthToken(of(A_SCOPE));
        });

        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("invalid_client"));
    }
}
