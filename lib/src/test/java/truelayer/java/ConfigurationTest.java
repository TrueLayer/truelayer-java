package truelayer.java;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigurationTest {

    @Test
    @DisplayName("Should yeld a proper configuration class")
    public void itShouldYeldAProperConfigClass() throws ConfigurationException {
        var sut = new Configuration();

        assertEquals("test-client-id", sut.getClientId());
        assertEquals("test-client-secret", sut.getClientSecret());
        assertEquals("paydirect", sut.getScope());
        assertEquals("client_credentials", sut.getGrantType());
        assertEquals("https://whatev.er/connect/token", sut.getTokenEndpoint());
    }
}
