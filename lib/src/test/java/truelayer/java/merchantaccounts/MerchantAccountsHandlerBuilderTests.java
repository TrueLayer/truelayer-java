package truelayer.java.merchantaccounts;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationHandler;

class MerchantAccountsHandlerBuilderTests {

    @Test
    @DisplayName("It should yield a merchant accounts handler")
    public void itShouldYieldAnAuthenticationHandler() {
        IAuthenticationHandler authHandler = AuthenticationHandler.New()
                .versionInfo(getVersionInfo())
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .clientCredentials(getClientCredentials())
                .build();
        MerchantAccountsHandler merchantAccountsHandler = MerchantAccountsHandler.New()
                .versionInfo(getVersionInfo())
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .authenticationHandler(authHandler)
                .build();

        assertNotNull(merchantAccountsHandler.getMerchantAccountsApi());
    }

    @Test
    @DisplayName("It should throw and exception if environment is not set")
    public void itShouldThrowExceptionIfEnvironmentNotSet() {
        Throwable thrown = assertThrows(NullPointerException.class, () -> MerchantAccountsHandler.New()
                .versionInfo(getVersionInfo())
                .build());

        assertEquals("environment must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw and exception if version info are not set")
    public void itShouldThrowExceptionIfVersionInfoNotSet() {
        Throwable thrown = assertThrows(NullPointerException.class, () -> MerchantAccountsHandler.New()
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .build());

        assertEquals("version info file not present", thrown.getMessage());
    }
}
