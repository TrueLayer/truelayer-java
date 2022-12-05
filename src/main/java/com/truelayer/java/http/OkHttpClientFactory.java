package com.truelayer.java.http;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.ConnectionPoolOptions;
import com.truelayer.java.ConnectionPoolOptions.KeepAliveDuration;
import com.truelayer.java.SigningOptions;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.http.auth.AccessTokenInvalidator;
import com.truelayer.java.http.auth.AccessTokenManager;
import com.truelayer.java.http.auth.cache.ICredentialsCache;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.SignatureInterceptor;
import com.truelayer.java.http.interceptors.TrueLayerAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.http.interceptors.logging.SensitiveHeaderGuard;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import lombok.Value;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

@Value
public class OkHttpClientFactory {
    VersionInfoLoader versionInfoLoader;

    public OkHttpClient buildBaseApiClient(
            Duration timeout,
            ConnectionPoolOptions connectionPoolOptions,
            ExecutorService requestExecutor,
            Consumer<String> logMessageConsumer) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (isNotEmpty(timeout)) {
            clientBuilder.callTimeout(timeout);
        }

        if (isNotEmpty(connectionPoolOptions)) {
            KeepAliveDuration keepAliveDuration = connectionPoolOptions.getKeepAliveDuration();
            clientBuilder.connectionPool(new ConnectionPool(
                    connectionPoolOptions.getMaxIdleConnections(),
                    keepAliveDuration.getDuration(),
                    keepAliveDuration.getTimeUnit()));
        }

        if (isNotEmpty(requestExecutor)) {
            clientBuilder.dispatcher(new Dispatcher(requestExecutor));
        }

        // Setup logging if required
        if (isNotEmpty(logMessageConsumer)) {
            clientBuilder.addNetworkInterceptor(
                    new HttpLoggingInterceptor(logMessageConsumer, new SensitiveHeaderGuard()));
        }

        clientBuilder.addInterceptor(new TrueLayerAgentInterceptor(versionInfoLoader.load()));

        return clientBuilder.build();
    }

    public OkHttpClient buildAuthApiClient(OkHttpClient baseHttpClient, ClientCredentials clientCredentials) {

        if (isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }

        OkHttpClient.Builder clientBuilder = baseHttpClient.newBuilder();

        clientBuilder.addInterceptor(new IdempotencyKeyInterceptor());

        return clientBuilder.build();
    }

    public OkHttpClient buildPaymentsApiClient(
            OkHttpClient authApiHttpClient,
            IAuthenticationHandler authenticationHandler,
            SigningOptions signingOptions,
            ICredentialsCache credentialsCache) {
        // By using .newBuilder() we share internal OkHttpClient resources
        // we just need to add the signature and authentication interceptor
        // as all the others are inherited
        OkHttpClient.Builder paymentsHttpClientBuilder = authApiHttpClient.newBuilder();

        paymentsHttpClientBuilder.addInterceptor(new SignatureInterceptor(signingOptions));

        AccessTokenManager.AccessTokenManagerBuilder accessTokenManagerBuilder =
                AccessTokenManager.builder().authenticationHandler(authenticationHandler);

        // setup credentials caching if required
        if (isNotEmpty(credentialsCache)) {
            AccessTokenManager accessTokenManager =
                    accessTokenManagerBuilder.credentialsCache(credentialsCache).build();

            paymentsHttpClientBuilder
                    .addInterceptor(new AuthenticationInterceptor(accessTokenManager))
                    .authenticator(new AccessTokenInvalidator(accessTokenManager));
        } else {
            AccessTokenManager accessTokenManager = accessTokenManagerBuilder.build();
            paymentsHttpClientBuilder.addInterceptor(new AuthenticationInterceptor(accessTokenManager));
        }

        return paymentsHttpClientBuilder.build();
    }
}
