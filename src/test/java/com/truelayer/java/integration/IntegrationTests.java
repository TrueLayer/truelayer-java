package com.truelayer.java.integration;

import com.atlassian.ta.wiremockpactgenerator.WireMockPactGenerator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.Extension;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.RegisterExtension;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@WireMockTest
@Tag("integration")
public abstract class IntegrationTests {
    protected TrueLayerClient tlClient;

    /*@RegisterExtension
    static WireMockExtension wm = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicHttpsPort().extensions(new Extension() {
                @Override
                public String getName() {
                    return null;
                }
            }))
            .build();
*/
    @BeforeEach
    public void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {

        /*new WireMockServer().addMockServiceRequestListener(
                WireMockPactGenerator
                        .builder("the-consumer", "the-provider")
                        .build()
        );*/

        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(testEnvironment)
                .withCredentialsCaching()
                .build();
    }
}
