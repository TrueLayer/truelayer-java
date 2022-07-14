package com.truelayer.java.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

@Tag("integration")
@WireMockTest
public abstract class IntegrationTests {
    protected TrueLayerClient tlClient;

    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching()
                .build();
    }
}
