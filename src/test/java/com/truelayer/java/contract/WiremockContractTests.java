package com.truelayer.java.contract;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.truelayer.java.*;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.RegisterExtension;

@Tag("contract")
@Deprecated
public abstract class WiremockContractTests {
    protected TrueLayerClient tlClient;

    @RegisterExtension
    protected static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig()
                    .notifier(new ConsoleNotifier(true)) // todo: review, debugging purposes
                    .dynamicPort()
                    .dynamicHttpsPort()
                    .extensions(WiremockPactExtension.builder().build()))
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
                .withHttpLogs()
                .build();
    }
}
