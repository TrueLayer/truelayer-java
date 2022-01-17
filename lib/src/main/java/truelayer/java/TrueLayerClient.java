package truelayer.java;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.ObjectUtils;
import truelayer.java.Constants.VersionInfo;
import truelayer.java.auth.AuthenticationHandler;
import truelayer.java.auth.IAuthenticationApi;
import truelayer.java.auth.IAuthenticationHandler;
import truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import truelayer.java.http.HttpClientBuilder;
import truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import truelayer.java.http.interceptors.SignatureInterceptor;
import truelayer.java.http.interceptors.UserAgentInterceptor;
import truelayer.java.payments.IPaymentHandler;
import truelayer.java.payments.IPaymentsApi;
import truelayer.java.payments.PaymentHandler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;
import static truelayer.java.Constants.ConfigurationKeys.*;

public class TrueLayerClient implements ITrueLayerClient {
    private final ClientCredentials clientCredentials;
    private final Optional<SigningOptions> signingOptions;
    private final boolean useSandbox;
    private PropertiesConfiguration versionInfo;
    private PropertiesConfiguration configuration;

    private IAuthenticationHandler authenticationHandler;
    private IPaymentHandler paymentHandler;
    private IHostedPaymentPageLinkBuilder hppBuilder;

    public TrueLayerClient(ClientCredentials clientCredentials,
                           Optional<SigningOptions> signingOptions, boolean useSandbox) {
        this.clientCredentials = clientCredentials;
        this.signingOptions = signingOptions;
        this.useSandbox = useSandbox;

        try {
            this.versionInfo = new Configurations().properties("version.properties");
        } catch (ConfigurationException e) {
            new TrueLayerException("Unable to load version info", e);
        }

        try {
            this.configuration = new Configurations().properties("application.properties");
        } catch (ConfigurationException e) {
            new TrueLayerException("Unable to load configuration", e);
        }
    }

    public static TrueLayerClientBuilder builder() {
        return new TrueLayerClientBuilder();
    }

    @Override
    public IAuthenticationHandler auth() {
        if (ObjectUtils.isEmpty(this.authenticationHandler)) {
            notNull(clientCredentials, "client credentials options must be set.");
            notEmpty(clientCredentials.getClientId(), "client id must be not empty");
            notEmpty(clientCredentials.getClientSecret(), "client secret must be not empty.");

            var interceptors = List.of(
                    new IdempotencyKeyInterceptor(),
                    getUserAgentInterceptor(),
                    getLoggingInterceptor());

            var httpClient = new HttpClientBuilder()
                    .baseUrl(getAuthEndpointUrl())
                    .applicationInterceptors(interceptors)
                    .build();

            this.authenticationHandler = AuthenticationHandler.builder()
                    .authenticationApi(httpClient.create(IAuthenticationApi.class))
                    .clientCredentials(clientCredentials)
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

            var interceptors = List.of(
                    new IdempotencyKeyInterceptor(),
                    getUserAgentInterceptor(),
                    new SignatureInterceptor(signingOptions),
                    getLoggingInterceptor());

            var httpClient = new HttpClientBuilder()
                    .baseUrl(getPaymentsEndpointUrl())
                    .applicationInterceptors(interceptors)
                    .build();

            this.paymentHandler = paymentHandlerBuilder.paymentsApi(httpClient.create(IPaymentsApi.class))
                    .paymentsScopes(getPaymentsScopes())
                    .build();
        }
        return this.paymentHandler;
    }

    @Override
    public IHostedPaymentPageLinkBuilder hpp() {
        if (ObjectUtils.isEmpty(this.hppBuilder)) {
            this.hppBuilder = HostedPaymentPageLinkBuilder.builder()
                    .endpoint(getHppEndpointUrl())
                    .build();
        }
        return this.hppBuilder;
    }

    public boolean useSandbox() {
        return this.useSandbox;
    }

    private String getAuthEndpointUrl() {
        var endpointKey = useSandbox ? AUTH_ENDPOINT_URL_SANDBOX : AUTH_ENDPOINT_URL_LIVE;
        return this.configuration.getString(endpointKey);
    }

    private String getPaymentsEndpointUrl() {
        var endpointKey = useSandbox ? PAYMENTS_ENDPOINT_URL_SANDBOX : PAYMENTS_ENDPOINT_URL_LIVE;
        return this.configuration.getString(endpointKey);
    }

    private String getHppEndpointUrl() {
        var endpointKey = useSandbox ? HPP_ENDPOINT_URL_SANDBOX : HPP_ENDPOINT_URL_LIVE;
        return this.configuration.getString(endpointKey);
    }

    private Interceptor getUserAgentInterceptor(){
        return new UserAgentInterceptor(
                this.versionInfo.getString(VersionInfo.NAME),
                this.versionInfo.getString(VersionInfo.VERSION));
    }

    private Interceptor getLoggingInterceptor(){
        //todo replace with non deprecated or custom implementation
        var loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private List<String> getPaymentsScopes() {
        return unmodifiableList(this.configuration.getList(String.class, PAYMENTS_SCOPES));
    }
}
