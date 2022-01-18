package truelayer.java.auth;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static retrofit2.Response.success;
import static truelayer.java.TestUtils.getClientCredentials;
import static truelayer.java.TestUtils.stubApiResponse;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TestUtils;
import truelayer.java.TrueLayerException;

public class AuthenticationHandlerTests {

    public static final String A_SCOPE = "paydirect";

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken() throws IOException, TrueLayerException {
        var token = TestUtils.buildAccessToken();
        var authentication = AuthenticationHandler.builder()
                .authenticationApi((clientId, clientSecret, grantType, scopes) -> stubApiResponse(success(token)))
                .clientCredentials(getClientCredentials())
                .build();

        var oauthToken = authentication.getOauthToken(of(A_SCOPE));

        assertEquals(token, oauthToken);
    }
}
