package truelayer.java.auth;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.exceptions.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationTests {

    public static final String A_GRANT_TYPE = "client_credentials";
    public static final String A_SCOPE = "paydirect";
    public static final String A_CLIENT_ID = "lucabaggi-703781";
    public static final String A_SECRET = "<a_real_secret>";

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken() throws AuthenticationException, ConfigurationException {
        var sut = new Authentication();

        var token= sut.getOauthToken(A_GRANT_TYPE, A_SCOPE, A_CLIENT_ID, A_SECRET);

        assertNotNull(token);
    }
}
