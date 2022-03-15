package com.truelayer.java;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.RetrofitFactory;
import com.truelayer.java.http.auth.AccessTokenInvalidator;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.auth.cache.SimpleAccessTokenCache;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.SignatureInterceptor;
import com.truelayer.java.http.interceptors.UserAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.DefaultLogConsumer;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.http.interceptors.logging.SensitiveHeaderGuard;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.payments.IPaymentsApi;
import com.truelayer.java.recurringpayments.IMandatesApi;
import com.truelayer.java.versioninfo.VersionInfo;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.time.Clock;
import java.util.Optional;
import java.util.function.Consumer;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.ObjectUtils;

/**
 * Builder class for TrueLayerClient instances.
 */
public class TrueLayerClientBuilder {
    private ClientCredentials clientCredentials;

    private SigningOptions signingOptions;

    // By default, production is used
    private Environment environment = Environment.live();

    private Optional<Consumer<String>> logMessageConsumer = Optional.empty();

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
        this.logMessageConsumer = Optional.of(new DefaultLogConsumer());
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
        this.logMessageConsumer = Optional.of(logConsumer);
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
        logMessageConsumer.ifPresent(logConsumer -> clientBuilder.addNetworkInterceptor(
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

        if (isEmpty(signingOptions)) {
            // return TL client with just authentication and HPP utils enabled
            return new TrueLayerClient(authenticationHandler, hppLinkBuilder);
        }

        AccessTokenManager accessTokenManager = AccessTokenManager.builder()
                .authenticationHandler(authenticationHandler)
                .accessTokenCache(new SimpleAccessTokenCache(Clock.systemUTC()))
                .build();

        // By using .newBuilder() we share internal OkHttpClient resources
        // we just need to add the signature and authentication interceptor
        // as all the others are inherited
        OkHttpClient paymentsHttpClient = authHttpClient
                .newBuilder()
                .addInterceptor(new SignatureInterceptor(signingOptions))
                .addInterceptor(new AuthenticationInterceptor(accessTokenManager))
                .authenticator(new AccessTokenInvalidator(accessTokenManager))
                .build();

        IPaymentsApi paymentsHandler = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IPaymentsApi.class);

        IMerchantAccountsApi merchantAccountsHandler = RetrofitFactory.build(
                        paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IMerchantAccountsApi.class);

        IMandatesApi mandatesHandler = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IMandatesApi.class);

        return new TrueLayerClient(
                authenticationHandler, paymentsHandler, merchantAccountsHandler, mandatesHandler, hppLinkBuilder);
    }
}
