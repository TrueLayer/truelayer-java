package com.truelayer.java;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.hpp.HostedPaymentPageLinkBuilder;
import com.truelayer.java.hpp.IHostedPaymentPageLinkBuilder;
import com.truelayer.java.http.RetrofitFactory;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.auth.AccessTokenManager.AccessTokenManagerBuilder;
import com.truelayer.java.http.auth.cache.SimpleAccessTokenCache;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.SignatureInterceptor;
import com.truelayer.java.http.interceptors.UserAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.merchantaccounts.IMerchantAccountsApi;
import com.truelayer.java.payments.IPaymentsApi;
import com.truelayer.java.versioninfo.VersionInfo;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.time.Clock;
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

    private boolean logEnabled;

    private boolean tokenCacheEnabled = true;

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
     * Utility to enable default logs for HTTP traces. Produced logs are not
     * leaking sensitive information
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder withHttpLogs() {
        this.logEnabled = true;
        return this;
    }

    /**
     * Utility to disable the default access token caching mechanism.
     * All calls requiring authentication will fire an Oauth request internally
     * @return the instance of the client builder used.
     */
    public TrueLayerClientBuilder disableTokenCache() {
        this.tokenCacheEnabled = false;
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

        if (logEnabled) {
            clientBuilder.addNetworkInterceptor(HttpLoggingInterceptor.New());
        }

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

        AccessTokenManagerBuilder accessTokenManagerBuilder =
                AccessTokenManager.builder().authenticationHandler(authenticationHandler);

        if (tokenCacheEnabled) {
            accessTokenManagerBuilder.accessTokenCache(new SimpleAccessTokenCache(Clock.systemUTC()));
        }

        // By using .newBuilder() we share internal OkHttpClient resources
        // we just need to add the signature and authentication interceptor
        // as all the others are inherited
        OkHttpClient paymentsHttpClient = authHttpClient
                .newBuilder()
                .addInterceptor(new SignatureInterceptor(signingOptions))
                .addInterceptor(new AuthenticationInterceptor(accessTokenManagerBuilder.build()))
                .build();

        IPaymentsApi paymentsHandler = RetrofitFactory.build(paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IPaymentsApi.class);

        IMerchantAccountsApi merchantAccountsHandler = RetrofitFactory.build(
                        paymentsHttpClient, environment.getPaymentsApiUri())
                .create(IMerchantAccountsApi.class);

        return new TrueLayerClient(authenticationHandler, paymentsHandler, merchantAccountsHandler, hppLinkBuilder);
    }
}
