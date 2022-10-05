package com.truelayer.java.http;

import static com.truelayer.java.TestUtils.getClientCredentials;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.truelayer.java.ConnectionPoolOptions;
import com.truelayer.java.TestUtils;
import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.http.auth.cache.SimpleCredentialsCache;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.SignatureInterceptor;
import com.truelayer.java.http.interceptors.UserAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.versioninfo.VersionInfo;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OkHttpClientFactoryTests {

    @Test
    @DisplayName("It should build a Base API client")
    public void shouldCreateABaseAuthApiClient() {
        Duration customTimeout = Duration.ofSeconds(2);
        ExecutorService customExecutor = Executors.newCachedThreadPool();
        Consumer<String> customLogMessageConsumer = System.out::println;

        OkHttpClient baseApiClient = getOkHttpClientFactory()
                .buildBaseApiClient(
                        customTimeout,
                        ConnectionPoolOptions.builder().build(),
                        customExecutor,
                        customLogMessageConsumer);

        assertNotNull(baseApiClient);
        assertTrue(
                baseApiClient.interceptors().stream().anyMatch(i -> i.getClass().equals(UserAgentInterceptor.class)),
                "User agent interceptor not found");
        assertTrue(
                baseApiClient.networkInterceptors().stream()
                        .anyMatch(i -> i.getClass().equals(HttpLoggingInterceptor.class)),
                "Logging interceptor not found");
        assertEquals(customExecutor, baseApiClient.dispatcher().executorService(), "Custom executor not found");
        assertEquals(customTimeout.toMillis(), baseApiClient.callTimeoutMillis(), "Unexpected call timeout configured");
    }

    @Test
    @DisplayName("It should build an Auth API client")
    public void shouldCreateAnAuthApiClient() {
        Duration customTimeout = Duration.ofSeconds(2);
        ExecutorService customExecutor = Executors.newCachedThreadPool();
        Consumer<String> customLogMessageConsumer = System.out::println;

        OkHttpClient baseApiClient = getOkHttpClientFactory()
                .buildBaseApiClient(
                        customTimeout,
                        ConnectionPoolOptions.builder().build(),
                        customExecutor,
                        customLogMessageConsumer);

        OkHttpClient authClient = getOkHttpClientFactory().buildAuthApiClient(baseApiClient, getClientCredentials());

        assertNotNull(authClient);
        assertTrue(
                authClient.interceptors().stream().anyMatch(i -> i.getClass().equals(UserAgentInterceptor.class)),
                "User agent interceptor not found");
        assertTrue(
                authClient.networkInterceptors().stream()
                        .anyMatch(i -> i.getClass().equals(HttpLoggingInterceptor.class)),
                "Logging interceptor not found");
        assertEquals(customExecutor, authClient.dispatcher().executorService(), "Custom executor not found");
        assertEquals(customTimeout.toMillis(), authClient.callTimeoutMillis(), "Unexpected call timeout configured");
    }

    @Test
    @DisplayName("It should build a Payments API client")
    public void shouldThrowCredentialsMissingException() {
        OkHttpClient baseHttpClient = getOkHttpClientFactory().buildBaseApiClient(null, null, null, null);
        OkHttpClient authClient = getOkHttpClientFactory().buildAuthApiClient(baseHttpClient, getClientCredentials());

        IAuthenticationHandler authenticationHandler = AuthenticationHandler.New()
                .clientCredentials(getClientCredentials())
                .httpClient(RetrofitFactory.build(authClient, URI.create("http://localhost")))
                .build();

        OkHttpClient paymentClient = getOkHttpClientFactory()
                .buildPaymentsApiClient(
                        authClient,
                        authenticationHandler,
                        TestUtils.getSigningOptions(),
                        new SimpleCredentialsCache(Clock.systemUTC()));

        assertTrue(
                paymentClient.interceptors().stream()
                        .anyMatch(i -> i.getClass().equals(IdempotencyKeyInterceptor.class)),
                "Idempotency interceptor not found");
        assertTrue(
                paymentClient.interceptors().stream().anyMatch(i -> i.getClass().equals(SignatureInterceptor.class)),
                "Signature interceptor not found");
        assertTrue(
                paymentClient.interceptors().stream()
                        .anyMatch(i -> i.getClass().equals(AuthenticationInterceptor.class)),
                "Authentication interceptor not found");
        assertNotNull(paymentClient.authenticator());
    }

    private OkHttpClientFactory getOkHttpClientFactory() {
        VersionInfo versionInfo = VersionInfo.builder()
                .libraryName("truelayer-java")
                .libraryVersion("1.0.0")
                .build();
        VersionInfoLoader versionInfoLoader = mock(VersionInfoLoader.class);
        when(versionInfoLoader.load()).thenReturn(versionInfo);

        return new OkHttpClientFactory(versionInfoLoader);
    }
}
