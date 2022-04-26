package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGeneratorUserOptions;
import com.github.tomakehurst.wiremock.http.*;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.RegisterExtension;

@Tag("integration")
public abstract class IntegrationTests {
    protected TrueLayerClient tlClient;

    @RegisterExtension
    protected static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig()
                    .dynamicPort()
                    .dynamicHttpsPort()
                    .extensions(WiremockPactExtension.builder()
                            .userOptions(new WireMockPactGeneratorUserOptions(
                                    "my-consumer",
                                    "my-provider",
                                    Collections.emptyList(),
                                    Collections.emptyList(),
                                    true,
                                    Collections.emptyList(),
                                    Collections.emptyList()))
                            .build()))
            .configureStaticDsl(true)
            .build();

    @BeforeEach
    public void setup() {
        Environment testEnvironment = TestUtils.getTestEnvironment(
                URI.create(wireMock.getRuntimeInfo().getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching()
                .build();
    }
}
