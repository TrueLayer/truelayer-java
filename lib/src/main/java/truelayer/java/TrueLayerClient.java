package truelayer.java;

import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.payments.IPaymentsApi;

/**
 * Main class that holds TrueLayer API client. Should be built with the help of its builder
 * class. Acts as entrypoint for the Java client library capabilities.
 *
 * @see TrueLayerClientBuilder
 */
public class TrueLayerClient implements ITrueLayerClient {
    private IAuthenticationHandler authenticationHandler;
    private IPaymentsApi paymentsHandler;
    private IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder;

    public TrueLayerClient(
            IAuthenticationHandler authenticationHandler, IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder) {
        this.authenticationHandler = authenticationHandler;
        this.hostedPaymentPageLinkBuilder = hostedPaymentPageLinkBuilder;
    }

    public TrueLayerClient(
            IAuthenticationHandler authenticationHandler,
            IPaymentsApi paymentsHandler,
            IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder) {
        this.authenticationHandler = authenticationHandler;
        this.paymentsHandler = paymentsHandler;
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
     * @inheritDoc
     */
    @Override
    public IAuthenticationHandler auth() {
        return this.authenticationHandler;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IPaymentsApi payments() {
        if (paymentsHandler == null) {
            throw new TrueLayerException(
                    "payment handler not initialized. Make sure you specified the required signing options while initializing the library");
        }
        return paymentsHandler;
    }

    /**
     * @inheritDoc
     */
    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        return this.hostedPaymentPageLinkBuilder;
    }
}
