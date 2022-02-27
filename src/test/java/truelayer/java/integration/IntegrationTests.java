package truelayer.java.integration;

import static truelayer.java.TestUtils.getClientCredentials;
import static truelayer.java.TestUtils.getSigningOptions;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import truelayer.java.Environment;
import truelayer.java.TestUtils;
import truelayer.java.TrueLayerClient;

@WireMockTest
@Tag("integration")
public abstract class IntegrationTests {
    protected static TrueLayerClient tlClient;

    @BeforeAll
    public static void setup(WireMockRuntimeInfo wireMockRuntimeInfo) {
        Environment testEnvironment = TestUtils.getTestEnvironment(URI.create(wireMockRuntimeInfo.getHttpBaseUrl()));

        tlClient = TrueLayerClient.New()
                .clientCredentials(getClientCredentials())
                .signingOptions(getSigningOptions())
                .environment(testEnvironment)
                .build();
    }
}
