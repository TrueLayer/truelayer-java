package truelayer.java.auth;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationHandlerBuilderTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    public void itShouldYieldAnAuthenticationHandler() {
        AuthenticationHandler handler = AuthenticationHandler.New()
                .versionInfo(getVersionInfo())
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .clientCredentials(getClientCredentials())
                .build();

        assertNotNull(handler.getAuthenticationApi());
        assertEquals(getClientCredentials(), handler.getClientCredentials());
    }

    @Test
    @DisplayName("It should throw and exception if credentials are missing")
    public void itShouldThrowExceptionIfCredentialsMissing() {
        Throwable thrown = assertThrows(NullPointerException.class, () -> AuthenticationHandler.New()
                .versionInfo(getVersionInfo())
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw and exception if environment is not set")
    public void itShouldThrowExceptionIfEnvironmentNotSet() {
        Throwable thrown = assertThrows(
                NullPointerException.class,
                () -> AuthenticationHandler.New().versionInfo(getVersionInfo()).build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw and exception if version info are not set")
    public void itShouldThrowExceptionIfVersionInfoNotSet() {
        Throwable thrown = assertThrows(NullPointerException.class, () -> AuthenticationHandler.New()
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }
}
