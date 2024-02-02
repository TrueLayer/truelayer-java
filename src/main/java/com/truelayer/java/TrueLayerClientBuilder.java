package com.truelayer.java;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.commonapi.CommonHandler;
import com.truelayer.java.commonapi.ICommonApi;
import com.truelayer.java.commonapi.ICommonHandler;
import com.truelayer.java.entities.RequestScopes;
import com.truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.OkHttpClientFactory;
import com.truelayer.java.http.RetrofitFactory;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.auth.cache.SimpleCredentialsCache;
import com.truelayer.java.http.interceptors.logging.DefaultLogConsumer;
import com.truelayer.java.mandates.IMandatesApi;
import com.truelayer.java.mandates.IMandatesHandler;
import com.truelayer.java.mandates.MandatesHandler;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.merchantaccounts.IMerchantAccountsHandler;
import com.truelayer.java.merchantaccounts.MerchantAccountsHandler;
import com.truelayer.java.payments.IPaymentsApi;
import com.truelayer.java.payments.IPaymentsHandler;
import com.truelayer.java.payments.PaymentsHandler;
import com.truelayer.java.paymentsproviders.IPaymentsProvidersApi;
import com.truelayer.java.paymentsproviders.IPaymentsProvidersHandler;
import com.truelayer.java.paymentsproviders.PaymentsProvidersHandler;
import com.truelayer.java.payouts.IPayoutsApi;
import com.truelayer.java.payouts.IPayoutsHandler;
import com.truelayer.java.payouts.PayoutsHandler;
import com.truelayer.java.versioninfo.LibraryInfoLoader;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import okhttp3.OkHttpClient;

/**
 * Builder class for TrueLayerClient instances.
 */
public class TrueLayerClientBuilder {
    private ClientCredentials clientCredentials;

    private RequestScopes globalScopes;

    private SigningOptions signingOptions;

    /**
     * Optional timeout configuration that defines a time limit for a complete HTTP call.
     * This includes resolving DNS, connecting, writing the request body, server processing, as well as
     * reading the response body. If not set, the internal HTTP client configuration are used.
     */
    private Duration timeout;

    /**
     * Optional configuration for internal connection pool.
     */
    private ConnectionPoolOptions connectionPoolOptions;

    /**
     * Optional execution service to be used by the internal HTTP client.
     */
    private ExecutorService requestExecutor;

    // By default, production is used
    private Environment environment = Environment.live();

    private Consumer<String> logMessageConsumer;

    private ICredentialsCache credentialsCache;

    private ProxyConfiguration proxyConfiguration;

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
     * @param signingOptions the signing options object that holds signature related information.
     * @return the instance of the client builder used.
     * @see SigningOptions
     */
    public TrueLayerClientBuilder signingOptions(SigningOptions signingOptions) {
        this.signingOptions = signingOptions;
        return this;
    }

    /**
     * Utility to set custom global scopes used by the library. If used, the specified scopes will override the
     * default scopes used by the library. If using this option, make sure to set valid scopes for all the API interactions
     * that your integration will have.
     * @param globalScopes custom global scopes to be used by the library for all authenticated endpoints.
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder withGlobalScopes(RequestScopes globalScopes) {
        this.globalScopes = globalScopes;
        return this;
    }

    /**
     * Utility to set a call timeout for the client.
     * @param timeout Optional timeout configuration that defines a time limit for a complete HTTP call.
     * This includes resolving DNS, connecting, writing the request body, server processing, as well as
     * reading the response body. If not set, the internal HTTP client configuration are used.
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder withTimeout(Duration timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Sets a connection pool for the internal HTTP client
     * @param connectionPoolOptions optional connection pool to be used
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder withConnectionPool(ConnectionPoolOptions connectionPoolOptions) {
        this.connectionPoolOptions = connectionPoolOptions;
        return this;
    }

    /**
     * Sets a custom HTTP request dispatcher for the internal HTTP client
     * @param requestExecutor an executor service responsible for handling the HTTP requests
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder withRequestExecutor(ExecutorService requestExecutor) {
        this.requestExecutor = requestExecutor;
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
     * Utility to enable default logs for HTTP traces.
     * @return the instance of the client builder used
     */
    public TrueLayerClientBuilder withHttpLogs() {
        this.logMessageConsumer = new DefaultLogConsumer();
        return this;
    }

    /**
     * Utility to enable custom logging for HTTP traces. Please notice that blocking
     * in the context of this consumer invocation will affect performance. An asynchronous implementation is
     * strongly advised.
     * @param logConsumer a custom log consumer
     * @return the instance of the client builder used
     */
    public TrueLayerClientBuilder withHttpLogs(Consumer<String> logConsumer) {
        this.logMessageConsumer = logConsumer;
        return this;
    }

    /**
     * Utility to enable default in memory caching for Oauth credentials.
     * @return the instance of the client builder used
     */
    public TrueLayerClientBuilder withCredentialsCaching() {
        this.credentialsCache = new SimpleCredentialsCache(Clock.systemUTC());
        return this;
    }

    /**
     * Utility to enable a custom cache for Oauth credentials.
     * @return the instance of the client builder used
     */
    public TrueLayerClientBuilder withCredentialsCaching(ICredentialsCache credentialsCache) {
        this.credentialsCache = credentialsCache;
        return this;
    }

    /**
     * Utility to configure a custom proxy, optionally including an authentication.
     * @param proxyConfiguration the configuration describing the custom proxy
     * @return the instance of the client builder used
     */
    public TrueLayerClientBuilder withProxyConfiguration(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
        return this;
    }

    /**
     * Builds the Java library main class to interact with TrueLayer APIs.
     * @return a client instance
     * @see TrueLayerClient
     */
    public TrueLayerClient build() {
        if (isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }

        OkHttpClientFactory httpClientFactory = new OkHttpClientFactory(new LibraryInfoLoader());

        OkHttpClient baseHttpClient = httpClientFactory.buildBaseApiClient(
                timeout, connectionPoolOptions, requestExecutor, logMessageConsumer, proxyConfiguration);

        OkHttpClient authHttpClient = httpClientFactory.buildAuthApiClient(baseHttpClient, clientCredentials);

        IAuthenticationHandler authenticationHandler = AuthenticationHandler.New()
                .clientCredentials(clientCredentials)
                .httpClient(RetrofitFactory.build(authHttpClient, environment.getAuthApiUri()))
                .build();

        IHostedPaymentPageLinkBuilder hppLinkBuilder =
                HostedPaymentPageLinkBuilder.New().uri(environment.getHppUri()).build();

        // We're reusing a client with only User agent and Idempotency key interceptors and give it our base payment
        // endpoint
        ICommonApi commonApi = RetrofitFactory.build(authHttpClient, environment.getPaymentsApiUri())
                .create(ICommonApi.class);
        ICommonHandler commonHandler = new CommonHandler(commonApi);

        // As per our RFC, if signing options is not configured we create a client which is able to interact
        // with the Authentication API only
        if (isEmpty(signingOptions)) {
            return new TrueLayerClient(authenticationHandler, hppLinkBuilder, commonHandler);
        }

        OkHttpClient paymentsHttpClient = httpClientFactory.buildPaymentsApiClient(
                authHttpClient, authenticationHandler, signingOptions, credentialsCache);

        IPaymentsApi paymentsApi = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IPaymentsApi.class);

        PaymentsHandler.PaymentsHandlerBuilder paymentsHandlerBuilder =
                PaymentsHandler.builder().paymentsApi(paymentsApi);
        if (customScopesPresent()) {
            paymentsHandlerBuilder.scopes(globalScopes);
        }
        IPaymentsHandler paymentsHandler = paymentsHandlerBuilder.build();

        OkHttpClient paymentsProvidersHttpClient = httpClientFactory.buildPaymentsApiClient(
                authHttpClient, authenticationHandler, signingOptions, credentialsCache);

        IPaymentsProvidersApi paymentsProvidersApi = RetrofitFactory.build(paymentsProvidersHttpClient, environment.getPaymentsApiUri())
                .create(IPaymentsProvidersApi.class);

        PaymentsProvidersHandler.PaymentsProvidersHandlerBuilder paymentsProvidersHandlerBuilder =
                PaymentsProvidersHandler.builder().paymentsProvidersApi(paymentsProvidersApi);
        if (customScopesPresent()) {
            paymentsProvidersHandlerBuilder.scopes(globalScopes);
        }
        IPaymentsProvidersHandler paymentsProvidersHandler = paymentsProvidersHandlerBuilder.build();

        IMerchantAccountsApi merchantAccountsApi = RetrofitFactory.build(
                        paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IMerchantAccountsApi.class);
        MerchantAccountsHandler.MerchantAccountsHandlerBuilder merchantAccountsHandlerBuilder =
                MerchantAccountsHandler.builder().merchantAccountsApi(merchantAccountsApi);
        if (customScopesPresent()) {
            merchantAccountsHandlerBuilder.scopes(globalScopes);
        }
        IMerchantAccountsHandler merchantAccountsHandler = merchantAccountsHandlerBuilder.build();

        IMandatesApi mandatesApi = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IMandatesApi.class);
        MandatesHandler.MandatesHandlerBuilder mandatesHandlerBuilder =
                MandatesHandler.builder().mandatesApi(mandatesApi);
        if (customScopesPresent()) {
            mandatesHandlerBuilder.scopes(globalScopes);
        }
        IMandatesHandler mandatesHandler = mandatesHandlerBuilder.build();

        IPayoutsApi payoutsApi = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IPayoutsApi.class);
        PayoutsHandler.PayoutsHandlerBuilder payoutsHandlerBuilder =
                PayoutsHandler.builder().payoutsApi(payoutsApi);
        if (customScopesPresent()) {
            merchantAccountsHandlerBuilder.scopes(globalScopes);
        }
        IPayoutsHandler payoutsHandler = payoutsHandlerBuilder.build();

        return new TrueLayerClient(
                authenticationHandler,
                paymentsHandler,
                paymentsProvidersHandler,
                merchantAccountsHandler,
                mandatesHandler,
                payoutsHandler,
                commonHandler,
                hppLinkBuilder);
    }

    private boolean customScopesPresent() {
        return isNotEmpty(globalScopes) && isNotEmpty(globalScopes.getScopes());
    }
}
