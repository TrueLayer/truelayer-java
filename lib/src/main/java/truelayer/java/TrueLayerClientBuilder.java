package truelayer.java;

import java.util.Optional;
import org.apache.commons.lang3.ObjectUtils;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.payments.PaymentHandler;
import truelayer.java.versioninfo.VersionInfo;
import truelayer.java.versioninfo.VersionInfoLoader;

/**
 * Builder class for TrueLayerClient instances.
 */
public class TrueLayerClientBuilder {
    private ClientCredentials clientCredentials;

    private Optional<SigningOptions> signingOptions = Optional.empty();

    // By default, production is used
    private Environment environment = Environment.live();

    TrueLayerClientBuilder() {}

    /**
     * Utility to set the client credentials required for Oauth2 protected endpoints.
     * @param credentials the credentials object that holds client id and secret.
     * @return the instance of the client builder used.
     * @see ClientCredentials
     */
    public TrueLayerClientBuilder clientCredentials(ClientCredentials credentials) {
        this.clientCredentials = credentials;
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
     * Utility to configure the library to interact a specific <i>TrueLayer</i> environment.
     * By default, <i>TrueLayer</i> production environment is used.
     * @param environment the environment to use
     * @return the instance of the client builder used.
     * @see Environment
     */
    public TrueLayerClientBuilder environment(Environment environment) {
        this.environment = environment;
        return this;
    }

    /**
     * Builds the Java library main class to interact with TrueLayer APIs.
     * @return a client instance
     * @see TrueLayerClient
     */
    public TrueLayerClient build() {
        VersionInfo versionInfo = new VersionInfoLoader().load();

        if (ObjectUtils.isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }

        IAuthenticationHandler authenticationHandler = AuthenticationHandler.New()
                .versionInfo(versionInfo)
                .environment(environment)
                .clientCredentials(clientCredentials)
                .build();

        PaymentHandler paymentsHandler = null;
        if (this.signingOptions.isPresent()) {
            SigningOptions signingOptions =
                    this.signingOptions.orElseThrow(() -> new TrueLayerException("signing options must be set"));

            paymentsHandler = PaymentHandler.New()
                    .versionInfo(versionInfo)
                    .environment(environment)
                    .signingOptions(signingOptions)
                    .authenticationHandler(authenticationHandler)
                    .build();
        }

        IHostedPaymentPageLinkBuilder hppBuilder =
                HostedPaymentPageLinkBuilder.New().uri(environment.getHppUri()).build();

        return new TrueLayerClient(authenticationHandler, Optional.ofNullable(paymentsHandler), hppBuilder);
    }
}
