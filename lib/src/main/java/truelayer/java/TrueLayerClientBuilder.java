package truelayer.java;

import java.util.Optional;
import truelayer.java.auth.AuthenticationHandlerBuilder;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.configuration.Configuration;
import truelayer.java.configuration.ConfigurationAssembler;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.payments.PaymentHandler;

/**
 * Builder class for TrueLayerClient instances.
 */
public class TrueLayerClientBuilder {
    private Optional<ClientCredentials> clientCredentials = Optional.empty();

    private Optional<SigningOptions> signingOptions = Optional.empty();

    // By default, production is used
    private boolean useSandbox;

    TrueLayerClientBuilder() {}

    /**
     * Utility to set the client credentials required for Oauth2 protected endpoints.
     * @param credentials the credentials object that holds client id and secret.
     * @return the instance of the client builder used.
     * @see ClientCredentials
     */
    public TrueLayerClientBuilder clientCredentials(ClientCredentials credentials) {
        this.clientCredentials = Optional.of(credentials);
        return this;
    }

    /**
     * Utility to set the signing options required for payments.
     * @param signingOptions the signing options object that holds signature related informations.
     * @return the instance of the client builder used.
     * @see SigningOptions
     */
    public TrueLayerClientBuilder signingOptions(SigningOptions signingOptions) {
        this.signingOptions = Optional.of(signingOptions);
        return this;
    }

    /**
     * Utility to configure the library to interact with TrueLayer sandbox environment.
     * By default, TrueLayer production environment is used.
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder useSandbox() {
        this.useSandbox = true;
        return this;
    }

    /**
     * Builds the Java library main class to interact with TrueLayer APIs.
     * @return a client instance
     * @see TrueLayerClient
     */
    public TrueLayerClient build() {
        Configuration configuration = new ConfigurationAssembler(this.useSandbox).assemble();

        ClientCredentials clientCredentials =
                this.clientCredentials.orElseThrow(() -> new TrueLayerException("client credentials must be set"));

        IAuthenticationHandler authenticationHandler = AuthenticationHandlerBuilder.New()
                .configuration(configuration)
                .clientCredentials(clientCredentials)
                .build();

        PaymentHandler paymentsHandler = null;
        if (this.signingOptions.isPresent()) {
            SigningOptions signingOptions =
                    this.signingOptions.orElseThrow(() -> new TrueLayerException("signing options must be set"));

            paymentsHandler = PaymentHandler.New()
                    .configuration(configuration)
                    .signingOptions(signingOptions)
                    .authenticationHandler(authenticationHandler)
                    .build();
        }

        IHostedPaymentPageLinkBuilder hppBuilder = HostedPaymentPageLinkBuilder.New()
                .endpoint(configuration.hostedPaymentPage().endpointUrl())
                .build();

        return new TrueLayerClient(authenticationHandler, Optional.ofNullable(paymentsHandler), hppBuilder);
    }
}
