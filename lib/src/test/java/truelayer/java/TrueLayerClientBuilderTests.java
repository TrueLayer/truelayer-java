package truelayer.java;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TrueLayerClientBuilderTests {
    @Test
    @DisplayName("It should build a live TrueLayer client")
    @SneakyThrows
    public void itShouldBuildALiveTrueLayerClient() {
        var trueLayerClient = TrueLayerClient.builder()
                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
                .build();

        assertFalse(trueLayerClient.useSandbox());
    }

    @Test
    @DisplayName("It should build a sandbox TrueLayer client")
    @SneakyThrows
    public void itShouldBuildASandboxTrueLayerClient() {
        var trueLayerClient = TrueLayerClient.builder()
                .clientCredentialsOptions(TestUtils.getClientCredentialsOptions())
                .useSandbox()
                .build();

        assertTrue(trueLayerClient.useSandbox());
    }
}