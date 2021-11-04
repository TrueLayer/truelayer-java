package truelayer.java.auth;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//todo: I see 2 possible approaches here for testsing. One being a more "unit" like test where
//we verify the ability of the SUT to create proper HTTP requests and handle responses, given specific inputs. I would wait the HTTP client
//to be injected ad SUT construction time before actually taking care.
//Additionally, we should define an integration tests and use Wiremock to stub Pay API responses.
//The latter could also be tested as part of a more generic integration test on the whole library
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
