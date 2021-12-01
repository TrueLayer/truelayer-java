package truelayer.java;

import lombok.SneakyThrows;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.lang3.ObjectUtils;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationApi;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.http.HttpClientFactory;
import truelayer.java.payments.IPaymentHandler;
import truelayer.java.payments.IPaymentsApi;
import truelayer.java.payments.PaymentHandler;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static truelayer.java.TrueLayerClient.ConfigurationKeys.*;

public class TrueLayerClient implements ITrueLayerClient {
    private final ClientCredentialsOptions clientCredentialsOptions;
    private final Optional<SigningOptions> signingOptions;
    private final PropertiesConfiguration configuration;
    private final boolean useSandbox;

    private IAuthenticationHandler authenticationHandler;
    private IPaymentHandler paymentHandler;

    @SneakyThrows //todo review
    public TrueLayerClient(ClientCredentialsOptions clientCredentialsOptions,
                           Optional<SigningOptions> signingOptions, boolean useSandbox) {
        this.clientCredentialsOptions = clientCredentialsOptions;
        this.signingOptions = signingOptions;
        this.useSandbox = useSandbox;

        this.configuration = new Configurations().properties("application.properties");
    }

    public static TrueLayerClientBuilder builder() {
        return new TrueLayerClientBuilder();
    }

    @Override
    @SneakyThrows //todo review
    public IAuthenticationHandler auth() {
        if (ObjectUtils.isEmpty(this.authenticationHandler)) {
            notNull(clientCredentialsOptions, "client credentials options must be set.");
            notEmpty(clientCredentialsOptions.getClientId(), "client id must be not empty");
            notEmpty(clientCredentialsOptions.getClientSecret(), "client secret must be not empty.");

            var authenticationApi = HttpClientFactory.getInstance()
                    .create(getAuthEndpointUrl()).create(IAuthenticationApi.class);

            this.authenticationHandler = AuthenticationHandler.builder()
                    .authenticationApi(authenticationApi)
                    .clientCredentialsOptions(clientCredentialsOptions)
                    .build();
        }
        return this.authenticationHandler;
    }

    @Override
    public IPaymentHandler payments() {
        if (ObjectUtils.isEmpty(this.paymentHandler)) {
            var paymentHandlerBuilder = PaymentHandler.builder()
                    .authenticationHandler(auth());

            var signingOptions = this.signingOptions.orElseThrow(() ->
                    new TrueLayerException("signing options must be set."));
            notEmpty(signingOptions.getKeyId(), "key id must be not empty");
            notNull(signingOptions.getPrivateKey(), "private key must be set.");

            var paymentsApi = HttpClientFactory.getInstance()
                    .create(getPaymentsEndpointUrl()).create(IPaymentsApi.class);
            this.paymentHandler = paymentHandlerBuilder.paymentsApi(paymentsApi)
                    .paymentsScopes(getPaymentsScopes())
                    .signingOptions(signingOptions)
                    .build();
        }
        return paymentHandler;
    }

    public boolean useSandbox(){
        return this.useSandbox;
    }

    private String getAuthEndpointUrl() {
        var endpointKey = useSandbox ? AUTH_ENDPOINT_URL_SANDBOX: AUTH_ENDPOINT_URL_LIVE;
        return this.configuration.getString(endpointKey);
    }

    private String getPaymentsEndpointUrl(){
        var endpointKey = useSandbox ? PAYMENTS_ENDPOINT_URL_SANDBOX: PAYMENTS_ENDPOINT_URL_LIVE;
        return this.configuration.getString(endpointKey);
    }

    private String[] getPaymentsScopes() {
        return this.configuration.getStringArray(PAYMENTS_SCOPES);
    }

    public static class ConfigurationKeys {
        public static final String AUTH_ENDPOINT_URL_LIVE = "tl.auth.endpoint.live";
        public static final String AUTH_ENDPOINT_URL_SANDBOX = "tl.auth.endpoint.sandbox";

        public static final String PAYMENTS_ENDPOINT_URL_LIVE = "tl.payments.endpoint.live";
        public static final String PAYMENTS_ENDPOINT_URL_SANDBOX = "tl.payments.endpoint.sandbox";
        public static final String PAYMENTS_SCOPES = "tl.payments.scopes";
    }

    /**
     * Builder class for TrueLayerClient instances. This is deliberately not managed
     * with Lombok annotations as its building phase is customized and slightly deviate from
     * the way Lombok builds stuff.
     */
    public static class TrueLayerClientBuilder {
        private ClientCredentialsOptions clientCredentialsOptions;

        private SigningOptions signingOptions;

        private boolean useSandbox;

        public TrueLayerClientBuilder() {

        }

        public TrueLayerClientBuilder clientCredentialsOptions(ClientCredentialsOptions credentialsOptions) {
            this.clientCredentialsOptions = credentialsOptions;
            return this;
        }

        public TrueLayerClientBuilder signingOptions(SigningOptions signingOptions) {
            this.signingOptions = signingOptions;
            return this;
        }

        public TrueLayerClientBuilder useSandbox() {
            this.useSandbox = true;
            return this;
        }

        public TrueLayerClient build() {
            var client = new TrueLayerClient(
                    this.clientCredentialsOptions,
                    ofNullable(this.signingOptions),
                    this.useSandbox
            );
            return client;
        }
    }
}
