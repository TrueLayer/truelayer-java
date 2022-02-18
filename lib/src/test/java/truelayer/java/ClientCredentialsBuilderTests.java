package truelayer.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClientCredentialsBuilderTests {

    public static final String A_CLIENT_ID = "a-client-id";
    public static final String A_CLIENT_SECRET = "a-client-secret";

    @Test
    @DisplayName("It should build client credentials")
    public void itShouldBuildAClientCredentialsInstance() {

        ClientCredentials clientCredentials = ClientCredentials.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_CLIENT_SECRET)
                .build();

        assertEquals(A_CLIENT_ID, clientCredentials.clientId);
        assertEquals(A_CLIENT_SECRET, clientCredentials.clientSecret);
    }

    @Test
    @DisplayName("It should throw an exception if client id is not set")
    public void itShouldThrowExceptionIfClientIdNotSet() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> ClientCredentials.builder()
                .clientId(null)
                .clientSecret(A_CLIENT_SECRET)
                .build());

        assertEquals("client id must be set", thrown.getMessage());
    }

    @Test
    @DisplayName("It should throw an exception if client secret is not set")
    public void itShouldThrowExceptionIfClientSecretNotSet() {
        Throwable thrown = assertThrows(TrueLayerException.class, () -> ClientCredentials.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(null)
                .build());

        assertEquals("client secret must be set", thrown.getMessage());
    }
}
