package truelayer.java.auth;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.getClientCredentials;
import static truelayer.java.TestUtils.getConfiguration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationHandlerBuilderTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    public void itShouldYieldAnAuthenticationHandler() {
        var handler = AuthenticationHandlerBuilder.New()
                .configuration(getConfiguration())
                .clientCredentials(getClientCredentials())
                .build();

        assertNotNull(handler.getAuthenticationApi());
        assertEquals(getClientCredentials(), handler.getClientCredentials());
    }

    @Test
    @DisplayName("It should throw and exception if credentials are missing")
    public void itShouldThrowExceptionIfCredentialsMissing() {
        var thrown = assertThrows(NullPointerException.class, () -> AuthenticationHandlerBuilder.New()
                .configuration(getConfiguration())
                .build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }
}
