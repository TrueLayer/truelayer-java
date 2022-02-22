package truelayer.java;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.getClientCredentials;
import static truelayer.java.TestUtils.getSigningOptions;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.merchantaccounts.IMerchantAccountsApi;
import truelayer.java.payments.IPaymentsApi;

public class TrueLayerClientTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    @SneakyThrows
    public void itShouldBuildAnAuthenticationHandler() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        IAuthenticationHandler authenticationHandler = trueLayerClient.auth();

        assertNotNull(authenticationHandler);
    }

    @Test
    @DisplayName("It should yield the same instance of the authentication handler if auth() is called multiple times")
    @SneakyThrows
    public void itShouldYieldTheSameAuthenticationHandler() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        IAuthenticationHandler authenticationHandler1 = trueLayerClient.auth();
        IAuthenticationHandler authenticationHandler2 = trueLayerClient.auth();

        assertSame(authenticationHandler1, authenticationHandler2);
    }

    @Test
    @DisplayName("It should yield a payment handler")
    public void itShouldBuildAPaymentClient() {
        ITrueLayerClient trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .build();

        IPaymentsApi paymentsHandler = trueLayerClient.payments();

        assertNotNull(paymentsHandler);
    }

    @Test
    @DisplayName("It should yield the same instance of the payment handler if payment() is called multiple times")
    @SneakyThrows
    public void itShouldYieldTheSamePaymentHandler() {
        ITrueLayerClient trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .build();

        IPaymentsApi paymentHandler1 = trueLayerClient.payments();
        IPaymentsApi paymentHandler2 = trueLayerClient.payments();

        assertSame(paymentHandler1, paymentHandler2);
    }

    @Test
    @DisplayName("It should yield an HPP link builder")
    public void itShouldBuildAnHppLinkBuilder() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        IHostedPaymentPageLinkBuilder hppLinkBuilder = trueLayerClient.hpp();

        assertNotNull(hppLinkBuilder);
    }

    @Test
    @DisplayName("It should yield the same instance of the HPP link builder if hpp() is called multiple times")
    @SneakyThrows
    public void itShouldYieldTheSameHppLinkBuilder() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        IHostedPaymentPageLinkBuilder hpp1 = trueLayerClient.hpp();
        IHostedPaymentPageLinkBuilder hpp2 = trueLayerClient.hpp();

        assertSame(hpp1, hpp2);
    }

    @Test
    @DisplayName("It should throw an exception if credentials options are missing")
    public void itShouldBuildASandboxTrueLaterClient() {
        Throwable thrown = assertThrows(
                TrueLayerException.class, () -> TrueLayerClient.New().build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    @Test
    @DisplayName(
            "It should throw an initialization error when invoking the payments handler and signing options are missing")
    public void itShouldThrowIfPaymentsHadlerIsInvokedWithoutSigningOptions() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, trueLayerClient::payments);

        assertEquals(
                "payments handler not initialized. Make sure you specified the required signing options while initializing the library",
                thrown.getMessage());
    }

    @Test
    @DisplayName("It should yield a merchant accounts handler")
    public void itShouldYieldAMerchantAccountsHandler() {
        ITrueLayerClient trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .build();

        IMerchantAccountsApi merchantAccountsHandler = trueLayerClient.merchantAccounts();

        assertNotNull(merchantAccountsHandler);
    }

    @Test
    @DisplayName(
            "It should throw an initialization error when invoking the merchant accounts handler and signing options are missing")
    public void itShouldThrowIfMerchantAccountsHadlerIsInvokedWithoutSigningOptions() {
        ITrueLayerClient trueLayerClient =
                TrueLayerClient.New().clientCredentials(getClientCredentials()).build();

        Throwable thrown = assertThrows(TrueLayerException.class, trueLayerClient::merchantAccounts);

        assertEquals(
                "merchant accounts handler not initialized. Make sure you specified the required signing options while initializing the library",
                thrown.getMessage());
    }
}
