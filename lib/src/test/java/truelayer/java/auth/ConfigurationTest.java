package truelayer.java.auth;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {

    @Test
    @DisplayName("Should yeld a proper configuration class")
    public void itShouldYeldAProperConfigClass() throws ConfigurationException {
        var sut = new Configuration();

        Assertions.assertEquals("test-client-id", sut.getClientId());
        Assertions.assertEquals("test-client-secret", sut.getClientSecret());
    }
}
