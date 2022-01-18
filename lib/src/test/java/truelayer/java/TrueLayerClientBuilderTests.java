package truelayer.java;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TrueLayerClientBuilderTests {
    @Test
    @DisplayName("It should build a live TrueLayer client")
    @SneakyThrows
    public void itShouldBuildALiveTrueLayerClient() {
        var trueLayerClient = TrueLayerClient.builder()
                .clientCredentials(TestUtils.getClientCredentials())
                .build();

        assertFalse(trueLayerClient.useSandbox());
    }

    @Test
    @DisplayName("It should build a sandbox TrueLayer client")
    @SneakyThrows
    public void itShouldBuildASandboxTrueLayerClient() {
        var trueLayerClient = TrueLayerClient.builder()
                .clientCredentials(TestUtils.getClientCredentials())
                .useSandbox()
                .build();

        assertTrue(trueLayerClient.useSandbox());
    }
}
