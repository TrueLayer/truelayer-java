package com.truelayer.java;

import static com.truelayer.java.TestUtils.getClientCredentials;
import static com.truelayer.java.TestUtils.getSigningOptions;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.mandates.IMandatesHandler;
import com.truelayer.java.merchantaccounts.IMerchantAccountsHandler;
import com.truelayer.java.payments.IPaymentsApi;
import com.truelayer.java.payouts.IPayoutsApi;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TrueLayerClientTests {

    @Test
    @DisplayName("It should yield a client builder")
    public void itShouldYieldAClientBuilder() {
        TrueLayerClientBuilder builder = TrueLayerClient.New();

        assertNotNull(builder);
    }

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
                .signingOptions(getSigningOptions())
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
                .signingOptions(getSigningOptions())
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
    @DisplayName("It should yield a merchant accounts handler")
    public void itShouldYieldAMerchantAccountsHandler() {
        ITrueLayerClient trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .build();

        IMerchantAccountsHandler merchantAccountsHandler = trueLayerClient.merchantAccounts();

        assertNotNull(merchantAccountsHandler);
    }

    @Test
    @DisplayName("It should yield a mandates handler")
    public void itShouldYieldAMandatesHandler() {
        ITrueLayerClient trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .build();

        IMandatesHandler mandatesHandler = trueLayerClient.mandates();

        assertNotNull(mandatesHandler);
    }

    @Test
    @DisplayName("It should yield a payouts handler")
    public void itShouldYieldAPayoutsHandler() {
        ITrueLayerClient trueLayerClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .build();

        IPayoutsApi payoutsHandler = trueLayerClient.payouts();

        assertNotNull(payoutsHandler);
    }
}
