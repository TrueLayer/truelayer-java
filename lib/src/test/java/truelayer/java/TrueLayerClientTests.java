package truelayer.java;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.getClientCredentials;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.payments.PaymentHandler;

public class TrueLayerClientTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    @SneakyThrows
    public void itShouldBuildAnAuthenticationHandler() {
        var trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        var authenticationHandler = (AuthenticationHandler) trueLayerClient.auth();

        assertNotNull(authenticationHandler);
    }

    @Test
    @DisplayName("It should yield the same instance of the authentication handler if auth() is called multiple times")
    @SneakyThrows
    public void itShouldYieldTheSameAuthenticationHandler() {
        var trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        var authenticationHandler1 = trueLayerClient.auth();
        var authenticationHandler2 = trueLayerClient.auth();

        assertTrue(authenticationHandler1 == authenticationHandler2);
    }

    @Test
    @DisplayName("It should yield a payment handler")
    public void itShouldBuildAPaymentClient() {
        var trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .build();

        var paymentsHandler = (PaymentHandler) trueLayerClient.payments();

        assertNotNull(paymentsHandler);
        assertNotNull(paymentsHandler.getPaymentsApi());
    }

    @Test
    @DisplayName("It should yield the same instance of the payment handler if payment() is called multiple times")
    @SneakyThrows
    public void itShouldYieldTheSamePaymentHandler() {
        var trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .build();

        var paymentHandler1 = trueLayerClient.payments();
        var paymentHandler2 = trueLayerClient.payments();

        assertTrue(paymentHandler1 == paymentHandler2);
    }

    @Test
    @DisplayName("It should yield an HPP link builder")
    public void itShouldBuildAnHppLinkBuilder() {
        var trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        var hppLinkBuilder = (HostedPaymentPageLinkBuilder) trueLayerClient.hpp();

        assertNotNull(hppLinkBuilder);
        assertNotNull(hppLinkBuilder.getEndpoint());
    }

    @Test
    @DisplayName("It should yield the same instance of the HPP link builder if hpp() is called multiple times")
    @SneakyThrows
    public void itShouldYieldTheSameHppLinkBuilder() {
        var trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        var hpp1 = trueLayerClient.hpp();
        var hpp2 = trueLayerClient.hpp();

        assertTrue(hpp1 == hpp2);
    }

    @Test
    @DisplayName("It should throw an exception if credentials options are missing")
    public void itShouldBuildASandboxTrueLaterClient() {
        var thrown = assertThrows(
                TrueLayerException.class, () -> TrueLayerClient.New().build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw an exception if signing options are missing")
    public void itShouldBuildAnExceptionIfSigningOptionMissing() {
        var trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        var thrown = assertThrows(TrueLayerException.class, () -> trueLayerClient.payments());

        assertEquals(
                "payment handler not initialized. Make sure you specified the requried signing options while initializing the library",
                thrown.getMessage());
    }
}
