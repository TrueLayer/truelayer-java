package com.truelayer.java.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TrueLayerClient;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;

@Tag("contract")
@ExtendWith(PactConsumerTestExt.class)
public class ContractTests {
    protected TrueLayerClient tlClient;

    @BeforeEach
    public void setup(MockServer pactServer) {
        tlClient = TrueLayerClient.New()
                .clientCredentials(TestUtils.getClientCredentials())
                .signingOptions(TestUtils.getSigningOptions())
                .environment(TestUtils.getTestEnvironment(URI.create(pactServer.getUrl())))
                .withCredentialsCaching()
                .withHttpLogs()
                .build();
    }
}
