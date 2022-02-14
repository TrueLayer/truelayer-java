package truelayer.java.payments;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationHandler;

class PaymentHandlerBuilderTests {

    @Test
    @DisplayName("It should yield a payment handler")
    public void itShouldYieldAnAuthenticationHandler() {
        IAuthenticationHandler authHandler = AuthenticationHandler.New()
                .versionInfo(getVersionInfo())
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .clientCredentials(getClientCredentials())
                .build();
        PaymentHandler paymentHandler = PaymentHandler.New()
                .versionInfo(getVersionInfo())
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .signingOptions(getSigningOptions())
                .authenticationHandler(authHandler)
                .build();

        assertNotNull(paymentHandler.getPaymentsApi());
    }

    @Test
    @DisplayName("It should throw and exception if environment is not set")
    public void itShouldThrowExceptionIfEnvironmentNotSet() {
        Throwable thrown = assertThrows(
                NullPointerException.class,
                () -> PaymentHandler.New().versionInfo(getVersionInfo()).build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw and exception if version info are not set")
    public void itShouldThrowExceptionIfVersionInfoNotSet() {
        Throwable thrown = assertThrows(NullPointerException.class, () -> PaymentHandler.New()
                .environment(getTestEnvironment(URI.create("http://localhost")))
                .build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }
}
