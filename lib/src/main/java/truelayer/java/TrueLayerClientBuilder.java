package truelayer.java;

import java.util.Optional;
import truelayer.java.auth.AuthenticationHandlerBuilder;
import truelayer.java.configuration.Configuration;
import truelayer.java.configuration.ConfigurationAssembler;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.payments.PaymentHandler;

/**
 * Builder class for TrueLayerClient instances. This is deliberately not managed
 * with Lombok annotations as its building phase is customized and slightly deviate from
 * the way Lombok builds stuff.
 */
public class TrueLayerClientBuilder {
    private Optional<ClientCredentials> clientCredentials = Optional.empty();

    private Optional<SigningOptions> signingOptions = Optional.empty();

    // By default, production is used
    private boolean useSandbox;

    TrueLayerClientBuilder() {}

    public TrueLayerClientBuilder clientCredentials(ClientCredentials credentials) {
        this.clientCredentials = Optional.of(credentials);
        return this;
    }

    public TrueLayerClientBuilder signingOptions(SigningOptions signingOptions) {
        this.signingOptions = Optional.of(signingOptions);
        return this;
    }

    public TrueLayerClientBuilder useSandbox() {
        this.useSandbox = true;
        return this;
    }

    public TrueLayerClient build() {
        Configuration configuration = new ConfigurationAssembler(this.useSandbox).assemble();

        var clientCredentials =
                this.clientCredentials.orElseThrow(() -> new TrueLayerException("client credentials must be set"));

        var authenticationHandler = AuthenticationHandlerBuilder.New()
                .configuration(configuration)
                .clientCredentials(clientCredentials)
                .build();

        PaymentHandler paymentsHandler = null;
        if (this.signingOptions.isPresent()) {
            var signingOptions =
                    this.signingOptions.orElseThrow(() -> new TrueLayerException("signing options must be set"));

            paymentsHandler = PaymentHandler.New()
                    .configuration(configuration)
                    .signingOptions(signingOptions)
                    .authenticationHandler(authenticationHandler)
                    .build();
        }

        var hppBuilder = HostedPaymentPageLinkBuilder.New()
                .endpoint(configuration.hostedPaymentPage().endpointUrl())
                .build();

        return new TrueLayerClient(authenticationHandler, Optional.ofNullable(paymentsHandler), hppBuilder);
    }
}
