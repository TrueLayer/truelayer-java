package truelayer.java;

import static truelayer.java.common.Constants.ConfigurationKeys.*;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.payments.IPaymentHandler;

@RequiredArgsConstructor
public class TrueLayerClient implements ITrueLayerClient {
    private final IAuthenticationHandler authenticationHandler;
    private final Optional<IPaymentHandler> paymentHandler;
    private final IHostedPaymentPageLinkBuilder hostedPaymentPageLinkBuilder;

    public static TrueLayerClientBuilder New() {
        return new TrueLayerClientBuilder();
    }

    @Override
    public IAuthenticationHandler auth() {
        return this.authenticationHandler;
    }

    @Override
    public IPaymentHandler payments() {
        return paymentHandler.orElseThrow(
                () -> new TrueLayerException(
                        "payment handler not initialized. Make sure you specified the requried signing options while initializing the library"));
    }

    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        return this.hostedPaymentPageLinkBuilder;
    }
}
