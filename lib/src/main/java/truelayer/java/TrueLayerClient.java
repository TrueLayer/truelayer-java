package truelayer.java;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.payments.IPaymentHandler;

/**
 * Main class that holds TrueLayer API client. Should be built with the help of its builder
 * class. Acts as entrypoint for the Java client library capabilities.
 *
 * @see TrueLayerClientBuilder
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TrueLayerClient implements ITrueLayerClient {
    private final IAuthenticationHandler authenticationHandler;
    private final Optional<IPaymentHandler> paymentHandler;
    private final IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder;

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
    public IPaymentHandler payments() {
        return paymentHandler.orElseThrow(
                () -> new TrueLayerException(
                        "payment handler not initialized. Make sure you specified the required signing options while initializing the library"));
    }

    /**
     * @inheritDoc
     */
    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        return this.hostedPaymentPageLinkBuilder;
    }
}
