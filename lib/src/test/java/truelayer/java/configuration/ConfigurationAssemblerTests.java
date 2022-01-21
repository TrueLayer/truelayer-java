package truelayer.java.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigurationAssemblerTests {

    @Test
    @DisplayName("It should build a live configuration object")
    public void itShouldBuildALiveConfiguration() {
        var sut = new ConfigurationAssembler(false);

        var config = sut.assemble();

        assertEquals("https://auth.truelayer.com", config.authentication().endpointUrl());
        assertEquals("https://test-pay-api.truelayer.com", config.payments().endpointUrl());
        assertEquals(List.of("payments"), config.payments().scopes());
        assertEquals("https://payment.truelayer.com", config.hostedPaymentPage().endpointUrl());
    }

    @Test
    @DisplayName("It should build a sandbox configuration object")
    public void itShouldBuildASandboxConfiguration() {
        var sut = new ConfigurationAssembler(true);

        var config = sut.assemble();

        assertEquals(
                "https://auth.truelayer-sandbox.com", config.authentication().endpointUrl());
        assertEquals(
                "https://test-pay-api.truelayer-sandbox.com", config.payments().endpointUrl());
        assertEquals(List.of("payments"), config.payments().scopes());
        assertEquals(
                "https://payment.truelayer-sandbox.com",
                config.hostedPaymentPage().endpointUrl());
    }
}
