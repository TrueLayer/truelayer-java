package truelayer.java.auth;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerException;
import truelayer.java.auth.entities.AccessToken;

import java.io.IOException;
import java.util.UUID;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.getClientCredentialsOptions;
import static truelayer.java.TestUtils.stubApiResponse;

public class AuthenticationHandlerTests {

    public static final String A_SCOPE = "paydirect";

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken() throws IOException, TrueLayerException {
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
}
