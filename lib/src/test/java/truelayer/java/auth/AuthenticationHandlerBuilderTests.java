package truelayer.java.auth;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import truelayer.java.TrueLayerException;
import truelayer.java.http.HttpClientFactory;

class AuthenticationHandlerBuilderTests {

    @Test
    @DisplayName("It should yield an authentication handler")
    public void itShouldYieldAnAuthenticationHandler() {
        AuthenticationHandler handler = AuthenticationHandler.New()
                .httpClient(getTestHttpClient())
                .clientCredentials(getClientCredentials())
                .build();

        assertNotNull(handler.getAuthenticationApi());
        assertEquals(getClientCredentials(), handler.getClientCredentials());
    }

    @Test
    @DisplayName("It should throw and exception if credentials are missing")
    public void itShouldThrowExceptionIfCredentialsMissing() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> AuthenticationHandler.New()
                .httpClient(getTestHttpClient())
                .build());

        assertEquals("client credentials must be set", thrown.getMessage());
    }

    private Retrofit getTestHttpClient() {
        HttpClientFactory testHttpClientFactory = new HttpClientFactory(
                getTestEnvironment(URI.create("http://localhost")), getVersionInfo(), getSigningOptions());
        return testHttpClientFactory.newAuthApiHttpClient();
    }
}
