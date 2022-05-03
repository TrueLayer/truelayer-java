package com.truelayer.java;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.commonapi.ICommonApi;
import com.truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.RetrofitFactory;
import com.truelayer.java.http.auth.AccessTokenInvalidator;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.auth.AccessTokenManager.AccessTokenManagerBuilder;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.auth.cache.SimpleCredentialsCache;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.SignatureInterceptor;
import com.truelayer.java.http.interceptors.UserAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.DefaultLogConsumer;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.http.interceptors.logging.SensitiveHeaderGuard;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.payments.IPaymentsApi;
import com.truelayer.java.versioninfo.VersionInfo;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.time.Clock;
import java.util.Optional;
import java.util.function.Consumer;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Builder class for TrueLayerClient instances.
 */
public class TrueLayerClientBuilder {
    private ClientCredentials clientCredentials;

    private SigningOptions signingOptions;

    // By default, production is used
    private Environment environment = Environment.live();

    private Consumer<String> logMessageConsumer;

    private ICredentialsCache credentialsCache;

    TrueLayerClientBuilder() {}

    private Optional<Consumer<String>> getLogMessageConsumer() {
        return Optional.ofNullable(logMessageConsumer);
    }

    private Optional<ICredentialsCache> getCredentialsCache() {
        return Optional.ofNullable(credentialsCache);
    }

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
        this.signingOptions = signingOptions;
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
     * Builds the Java library main class to interact with TrueLayer APIs.
     * @return a client instance
     * @see TrueLayerClient
     */
    public TrueLayerClient build() {
        if (ObjectUtils.isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }

        VersionInfo versionInfo = new VersionInfoLoader().load();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        // Setup logging if required
        getLogMessageConsumer()
                .ifPresent(logConsumer -> clientBuilder.addNetworkInterceptor(
                        new HttpLoggingInterceptor(logConsumer, new SensitiveHeaderGuard())));

        clientBuilder.addInterceptor(new IdempotencyKeyInterceptor());
        clientBuilder.addInterceptor(new UserAgentInterceptor(versionInfo));

        OkHttpClient authHttpClient = clientBuilder.build();

        IAuthenticationHandler authenticationHandler = AuthenticationHandler.New()
                .clientCredentials(clientCredentials)
                .httpClient(RetrofitFactory.build(authHttpClient, environment.getAuthApiUri()))
                .build();

        IHostedPaymentPageLinkBuilder hppLinkBuilder =
                HostedPaymentPageLinkBuilder.New().uri(environment.getHppUri()).build();

        // We're reusing a client with only User agent and Idempotency key interceptors and give it our base payment
        // endpoint
        ICommonApi commonApiHandler = RetrofitFactory.build(authHttpClient, environment.getPaymentsApiUri())
                .create(ICommonApi.class);

        if (isEmpty(signingOptions)) {
            // return TL client with just authentication, HPP and common utils enabled
            return new TrueLayerClient(authenticationHandler, hppLinkBuilder, commonApiHandler);
        }

        // By using .newBuilder() we share internal OkHttpClient resources
        // we just need to add the signature and authentication interceptor
        // as all the others are inherited
        OkHttpClient.Builder paymentsHttpClientBuilder =
                authHttpClient.newBuilder().addInterceptor(new SignatureInterceptor(signingOptions));

        AccessTokenManagerBuilder accessTokenManagerBuilder =
                AccessTokenManager.builder().authenticationHandler(authenticationHandler);

        // setup credentials caching if required
        if (getCredentialsCache().isPresent()) {
            AccessTokenManager accessTokenManager = accessTokenManagerBuilder
                    .credentialsCache(getCredentialsCache().get())
                    .build();

            paymentsHttpClientBuilder
                    .addInterceptor(new AuthenticationInterceptor(accessTokenManager))
                    .authenticator(new AccessTokenInvalidator(accessTokenManager));
        } else {
            AccessTokenManager accessTokenManager = accessTokenManagerBuilder.build();
            paymentsHttpClientBuilder.addInterceptor(new AuthenticationInterceptor(accessTokenManager));
        }

        OkHttpClient paymentsHttpClient = paymentsHttpClientBuilder.build();

        IPaymentsApi paymentsHandler = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IPaymentsApi.class);

        IMerchantAccountsApi merchantAccountsHandler = RetrofitFactory.build(
                        paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IMerchantAccountsApi.class);

        return new TrueLayerClient(
                authenticationHandler, paymentsHandler, merchantAccountsHandler, hppLinkBuilder, commonApiHandler);
    }
}
