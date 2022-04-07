package com.truelayer.java;

import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.mandates.IMandatesApi;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.payments.IPaymentsApi;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Main class that holds TrueLayer API client. Should be built with the help of its builder
 * class. Acts as entrypoint for the Java client library capabilities.
 *
 * @see TrueLayerClientBuilder
 */
@AllArgsConstructor
public class TrueLayerClient implements ITrueLayerClient {
    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsHandler;
    private IMerchantAccountsApi merchantAccountsHandler;
    private IMandatesApi mandatesHandler;
    private IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder;

    public TrueLayerClient(
            IAuthenticationHandler authenticationHandler, IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder) {
        this.authenticationHandler = authenticationHandler;
        this.hostedPaymentPageLinkBuilder = hostedPaymentPageLinkBuilder;
    }

    /**
     * Static utility to return a builder instance.
     * @return an instance of the TrueLayer API client builder.
     */
    public static TrueLayerClientBuilder New() {
        return new TrueLayerClientBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IAuthenticationHandler auth() {
        return this.authenticationHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPaymentsApi payments() {
        if (ObjectUtils.isEmpty(paymentsHandler)) {
            throw buildInitializationException("payments");
        }
        return paymentsHandler;
    }

    @Override
    public IMerchantAccountsApi merchantAccounts() {
        if (ObjectUtils.isEmpty(merchantAccountsHandler)) {
            throw buildInitializationException("merchant accounts");
        }
        return merchantAccountsHandler;
    }

    @Override
    public IMandatesApi mandates() {
        if (ObjectUtils.isEmpty(mandatesHandler)) {
            throw buildInitializationException("mandates");
        }
        return mandatesHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        return this.hostedPaymentPageLinkBuilder;
    }

    private TrueLayerException buildInitializationException(String handlerName) {
        return new TrueLayerException(String.format(
                "%s handler not initialized."
                        + " Make sure you specified the required signing options while initializing the library",
                handlerName));
    }
}
