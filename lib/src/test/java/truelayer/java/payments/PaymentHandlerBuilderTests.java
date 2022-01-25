package truelayer.java.payments;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.AuthenticationHandlerBuilder;
import truelayer.java.auth.IAuthenticationHandler;

class PaymentHandlerBuilderTests {

    @Test
    @DisplayName("It should yield a payment handler")
    public void itShouldYieldAnAuthenticationHandler() {
        IAuthenticationHandler authHandler = AuthenticationHandlerBuilder.New()
                .configuration(getConfiguration())
                .clientCredentials(getClientCredentials())
                .build();
        PaymentHandler paymentHandler = PaymentHandler.New()
                .configuration(getConfiguration())
                .signingOptions(getSigningOptions())
                .authenticationHandler(authHandler)
                .build();

        assertNotNull(paymentHandler.getPaymentsApi());
    }

    @Test
    @DisplayName("It should throw and exception if signing options are missing")
    public void itShouldThrowExceptionIfCredentialsMissing() {
        Throwable thrown = assertThrows(
                NullPointerException.class,
                () -> PaymentHandler.New().configuration(getConfiguration()).build());

        assertEquals("signing options must be set", thrown.getMessage());
    }
}
