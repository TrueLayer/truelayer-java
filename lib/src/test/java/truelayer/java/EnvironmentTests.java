package truelayer.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EnvironmentTests {

    @Test
    @DisplayName("It should create a live environment")
    public void shouldCreateALiveEnvironment() {
        Environment environment = Environment.live();

        assertEquals("https://auth.truelayer.com", environment.getAuthApiUri().toString());
        assertEquals(
                "https://api.truelayer.com", environment.getPaymentsApiUri().toString());
        assertEquals("https://payment.truelayer.com", environment.getHppUri().toString());
    }

    @Test
    @DisplayName("It should create a sandbox environment")
    public void shouldCreateASandboxEnvironment() {
        Environment environment = Environment.sandbox();

        assertEquals(
                "https://auth.truelayer-sandbox.com",
                environment.getAuthApiUri().toString());
        assertEquals(
                "https://api.truelayer-sandbox.com",
                environment.getPaymentsApiUri().toString());
        assertEquals(
                "https://payment.truelayer-sandbox.com", environment.getHppUri().toString());
    }

    @Test
    @DisplayName("It should create a custom environment")
    public void shouldCreateACustomEnvironment() {
        URI customAuthUri = URI.create("http://localhost/auth");
        URI customPaymentsUri = URI.create("http://localhost/pay");
        URI customHppUri = URI.create("http://localhost/hpp");
        Environment environment = Environment.custom(customAuthUri, customPaymentsUri, customHppUri);

        assertEquals(customAuthUri, environment.getAuthApiUri());
        assertEquals(customPaymentsUri, environment.getPaymentsApiUri());
        assertEquals(customHppUri, environment.getHppUri());
    }
}
