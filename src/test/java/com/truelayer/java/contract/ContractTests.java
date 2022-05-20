package com.truelayer.java.contract;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.truelayer.java.Environment;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("contract")
@WireMockTest
@ExtendWith(PactConsumerTestExt.class)
public class ContractTests {
    protected TrueLayerClient tlClient;

    @BeforeEach
    public void setup(MockServer pactServer, WireMockRuntimeInfo wireMockRuntimeInfo) {
        // stub the auth server interaction via Wiremock, that is
        // outside the context of Pact. Our contract tests won't assert
        // on any Auth server interactions
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .withResponseBodyFile("auth/200.access_token.json")
                .build();

        Environment contractTestingEnvironment = Environment.custom(
                URI.create(wireMockRuntimeInfo.getHttpBaseUrl()),
                URI.create(pactServer.getUrl()),
                URI.create(pactServer.getUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(contractTestingEnvironment)
                .withCredentialsCaching()
                .build();
    }
}
