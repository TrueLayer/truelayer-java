package com.truelayer.java.http;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.truelayer.java.ClientCredentials;
import com.truelayer.java.ConnectionPoolOptions;
import com.truelayer.java.ConnectionPoolOptions.KeepAliveDuration;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.UserAgentInterceptor;
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

    public OkHttpClient buildAuthApiClient(
            ClientCredentials clientCredentials,
            Duration timeout,
            ConnectionPoolOptions connectionPoolOptions,
            ExecutorService requestExecutor,
            Consumer<String> logMessageConsumer) {
        if (isEmpty(clientCredentials)) {
            throw new TrueLayerException("client credentials must be set");
        }

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
            // todo: make sure we really want a net interceptor here
            clientBuilder.addNetworkInterceptor(
                    new HttpLoggingInterceptor(logMessageConsumer, new SensitiveHeaderGuard()));
        }

        clientBuilder.addInterceptor(new IdempotencyKeyInterceptor());
        clientBuilder.addInterceptor(new UserAgentInterceptor(versionInfoLoader.load()));

        return clientBuilder.build();
    }

    public static OkHttpClient buildPaymentsApiClient() {
        return null;
    }
}
