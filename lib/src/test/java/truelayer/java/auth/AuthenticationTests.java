package truelayer.java.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationTests {

    public static final String A_GRANT_TYPE = "client_credentials";
    public static final String A_SCOPE = "paydirect";
    public static final String A_CLIENT_ID = "adilisio-5241b0";
    public static final String A_SECRET = "whatever";

    @Test
    @DisplayName("It should yield and access token if correct credentials are supplied")
    public void itShouldYieldAnAccessToken(){
        var sut = new Authentication();

        var token= sut.getOauthToken(A_GRANT_TYPE, A_SCOPE, A_CLIENT_ID, A_SECRET);

        assertNotNull(token);
    }
}
