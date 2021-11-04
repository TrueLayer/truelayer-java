package truelayer.java.auth;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthenticationTests {

    public static final String A_SCOPE = "paydirect";
    public static final String A_CLIENT_ID = "<a_client_id>";
    public static final String A_SECRET = "<a_client_secret>";

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken() throws AuthenticationException, ConfigurationException {
        var sut = Authentication.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_SECRET)
                .build();

        var token= sut.getOauthToken(List.of(A_SCOPE));

        assertFalse(token.getAccessToken().isEmpty());
        assertFalse(token.getTokenType().isEmpty());
        assertFalse(token.getAccessToken().isEmpty());
        assertTrue(token.getExpiresIn() > 0);
    }
}
