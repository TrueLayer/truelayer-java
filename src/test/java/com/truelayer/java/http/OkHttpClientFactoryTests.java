package com.truelayer.java.http;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.truelayer.java.ConnectionPoolOptions;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerException;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.UserAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.versioninfo.VersionInfo;
import com.truelayer.java.versioninfo.VersionInfoLoader;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OkHttpClientFactoryTests {

    @Test
    @DisplayName("It should build an Auth API client")
    public void shouldCreateAnAuthApiClient() {
        Duration customTimeout = Duration.ofSeconds(2);
        ExecutorService customExecutor = Executors.newCachedThreadPool();
        Consumer<String> customLogMessageConsumer = System.out::println;

        OkHttpClient authClient = getOkHttpClientFactory()
                .buildAuthApiClient(
                        TestUtils.getClientCredentials(),
                        customTimeout,
                        ConnectionPoolOptions.builder().build(),
                        customExecutor,
                        customLogMessageConsumer);

        assertNotNull(authClient);
        assertTrue(
                authClient.interceptors().stream().anyMatch(i -> i.getClass().equals(IdempotencyKeyInterceptor.class)),
                "Idempotency interceptor not found");
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
    @DisplayName("It should throw an exception if credentials are missing")
    public void shouldThrowCredentialsMissingException() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> getOkHttpClientFactory()
                .buildAuthApiClient(null, null, null, null, null));

        assertEquals("client credentials must be set", thrown.getMessage());
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
