package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

@DisplayName("Timeout integration tests")
public class TimeoutIntegrationTests extends IntegrationTests {

    private static final int A_TIMEOUT_MS = 300;
    private static final int API_RESPONSE_TIME_MS = 1000;

    // overrides the default setup by setting a custom call timeout
    @BeforeEach
    @Override
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .withTimeout(Duration.ofMillis(A_TIMEOUT_MS))
                .build();
    }

    @SneakyThrows
    @Test
    @DisplayName("It should throw a timeout exception")
    public void shouldTimeout() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile("auth/200.access_token.json")
                .delayMs(API_RESPONSE_TIME_MS)
                .build();

        assertThrows(
                ExecutionException.class,
                tlClient.auth().getOauthToken(Collections.singletonList("a-scope"))::get,
                "timeout");
    }
}
