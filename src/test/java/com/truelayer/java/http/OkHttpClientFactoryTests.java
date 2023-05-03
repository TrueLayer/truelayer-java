package com.truelayer.java.http;

import static com.truelayer.java.Constants.HeaderNames.PROXY_AUTHORIZATION;
import static com.truelayer.java.TestUtils.getClientCredentials;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.truelayer.java.ConnectionPoolOptions;
import com.truelayer.java.ProxyConfigurations;
import com.truelayer.java.TestUtils;
import com.truelayer.java.auth.AuthenticationHandler;
import com.truelayer.java.auth.IAuthenticationHandler;
import com.truelayer.java.http.auth.cache.SimpleCredentialsCache;
import com.truelayer.java.http.interceptors.AuthenticationInterceptor;
import com.truelayer.java.http.interceptors.IdempotencyKeyInterceptor;
import com.truelayer.java.http.interceptors.SignatureInterceptor;
import com.truelayer.java.http.interceptors.TrueLayerAgentInterceptor;
import com.truelayer.java.http.interceptors.logging.HttpLoggingInterceptor;
import com.truelayer.java.versioninfo.LibraryInfoLoader;
import com.truelayer.java.versioninfo.VersionInfo;
import java.net.Proxy;
import java.net.URI;
import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import okhttp3.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OkHttpClientFactoryTests {

    private static final Duration DEFAULT_OK_HTTP_CLIENT_CONNECT_WRITE_READ_TIMEOUT = Duration.ofSeconds(10);

    @Test
    @DisplayName("It should build a Base API client")
    public void shouldCreateABaseAuthApiClient() {
        ExecutorService customExecutor = Executors.newCachedThreadPool();
        Consumer<String> customLogMessageConsumer = System.out::println;

        OkHttpClient baseApiClient = getOkHttpClientFactory()
                .buildBaseApiClient(
                        null, ConnectionPoolOptions.builder().build(), customExecutor, customLogMessageConsumer, null);

        assertNotNull(baseApiClient);
        assertTrue(
                baseApiClient.interceptors().stream()
                        .anyMatch(i -> i.getClass().equals(TrueLayerAgentInterceptor.class)),
                "User agent interceptor not found");
        assertTrue(
                baseApiClient.networkInterceptors().stream()
                        .anyMatch(i -> i.getClass().equals(HttpLoggingInterceptor.class)),
                "Logging interceptor not found");
        assertEquals(customExecutor, baseApiClient.dispatcher().executorService(), "Custom executor not found");
        assertEquals(
                DEFAULT_OK_HTTP_CLIENT_CONNECT_WRITE_READ_TIMEOUT.toMillis(),
                baseApiClient.connectTimeoutMillis(),
                "Unexpected connect timeout configured");
        assertEquals(
                OkHttpClientFactory.DEFAULT_TL_CLIENT_READ_TIMEOUT.toMillis(),
                baseApiClient.readTimeoutMillis(),
                "Unexpected read timeout configured");
        assertEquals(
                DEFAULT_OK_HTTP_CLIENT_CONNECT_WRITE_READ_TIMEOUT.toMillis(),
                baseApiClient.writeTimeoutMillis(),
                "Unexpected write timeout configured");
        assertEquals(0, baseApiClient.callTimeoutMillis(), "Unexpected call timeout configured");
    }

    @Test
    @DisplayName("It should build a Base API client with custom call timeout")
    public void shouldCreateABaseAuthApiClientWithCustomCallTimeout() {
        Duration customTimeout = Duration.ofSeconds(2);
        ExecutorService customExecutor = Executors.newCachedThreadPool();
        Consumer<String> customLogMessageConsumer = System.out::println;

        OkHttpClient baseApiClient = getOkHttpClientFactory()
                .buildBaseApiClient(
                        customTimeout,
                        ConnectionPoolOptions.builder().build(),
                        customExecutor,
                        customLogMessageConsumer,
                        null);

        assertNotNull(baseApiClient);
        assertEquals(0, baseApiClient.connectTimeoutMillis(), "Unexpected connect timeout configured");
        assertEquals(0, baseApiClient.readTimeoutMillis(), "Unexpected read timeout configured");
        assertEquals(0, baseApiClient.writeTimeoutMillis(), "Unexpected write timeout configured");
        assertEquals(customTimeout.toMillis(), baseApiClient.callTimeoutMillis(), "Unexpected call timeout configured");
    }

    @SneakyThrows
    @Test
    @DisplayName("It should build a Base API client with custom unauthenticated proxy configuration")
    public void shouldCreateABaseAuthApiClientWithCustomProxyConfigNoAuthentication() {
        ProxyConfigurations customProxyConfig =
                ProxyConfigurations.builder().hostname("127.0.0.1").port(9999).build();

        OkHttpClient baseApiClient = getOkHttpClientFactory()
                .buildBaseApiClient(null, ConnectionPoolOptions.builder().build(), null, null, customProxyConfig);

        assertNotNull(baseApiClient);
        Proxy configuredProxy = baseApiClient.proxy();
        assertNotNull(configuredProxy, "proxy not configured");
        assertEquals(Proxy.Type.HTTP, configuredProxy.type(), "unexpected proxy type configured");
        assertTrue(
                configuredProxy
                        .address()
                        .toString()
                        .endsWith(customProxyConfig.hostname() + ":" + customProxyConfig.port()),
                "unexpected proxy address found");
        Authenticator proxyAuthenticator = baseApiClient.proxyAuthenticator();
        // make sure the authenticator is the default used by OkHttp.
        assertInstanceOf(Authenticator.NONE.getClass(), proxyAuthenticator);
    }

    @SneakyThrows
    @Test
    @DisplayName("It should build a Base API client with custom proxy configuration with authentication")
    public void shouldCreateABaseAuthApiClientWithCustomProxyConfigWithAuthentication() {
        ProxyConfigurations customProxyConfig = ProxyConfigurations.builder()
                .hostname("127.0.0.1")
                .port(9999)
                .credentials(ProxyConfigurations.Credentials.builder()
                        .username("john")
                        .password("doe")
                        .build())
                .build();

        OkHttpClient baseApiClient = getOkHttpClientFactory()
                .buildBaseApiClient(null, ConnectionPoolOptions.builder().build(), null, null, customProxyConfig);

        assertNotNull(baseApiClient);
        Proxy configuredProxy = baseApiClient.proxy();
        assertNotNull(configuredProxy, "proxy not configured");
        assertEquals(Proxy.Type.HTTP, configuredProxy.type(), "unexpected proxy type configured");
        assertTrue(
                configuredProxy
                        .address()
                        .toString()
                        .endsWith(customProxyConfig.hostname() + ":" + customProxyConfig.port()),
                "unexpected proxy address found");
        Authenticator proxyAuthenticator = baseApiClient.proxyAuthenticator();
        assertNotNull(proxyAuthenticator, "proxy authenticator not configured");
        Request testRequest = proxyAuthenticator.authenticate(
                null,
                new Response.Builder()
                        .protocol(Protocol.HTTP_2)
                        .code(200)
                        .message("A response")
                        .request(new Request.Builder()
                                .url("https://localhost/test")
                                .get()
                                .build())
                        .build());
        assertNotNull(testRequest, "invalid request generated");
        String expectedProxyAuthHeader = Credentials.basic(
                customProxyConfig.credentials().username(),
                customProxyConfig.credentials().password());
        assertEquals(
                expectedProxyAuthHeader,
                testRequest.header(PROXY_AUTHORIZATION),
                "unexpected Proxy-Authentication header found");
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
                        customLogMessageConsumer,
                        null);

        OkHttpClient authClient = getOkHttpClientFactory().buildAuthApiClient(baseApiClient, getClientCredentials());

        assertNotNull(authClient);
        assertTrue(
                authClient.interceptors().stream().anyMatch(i -> i.getClass().equals(IdempotencyKeyInterceptor.class)),
                "Idempotency interceptor not found");
        assertTrue(
                authClient.interceptors().stream().anyMatch(i -> i.getClass().equals(TrueLayerAgentInterceptor.class)),
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
        OkHttpClient baseHttpClient = getOkHttpClientFactory().buildBaseApiClient(null, null, null, null, null);
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
        LibraryInfoLoader libraryInfoLoader = mock(LibraryInfoLoader.class);
        when(libraryInfoLoader.load()).thenReturn(versionInfo);

        return new OkHttpClientFactory(libraryInfoLoader);
    }
}
