package truelayer.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerClient.SigningOptions;

import java.io.IOException;

public class TrueLayerClientTests {

    public static final String A_CLIENT_ID = "<a_client_id>";
    public static final String A_CLIENT_SECRET = "<a_real_secret>";
    public static final String A_SIGNING_KEY_ID = "<a_real_key_id>";
    public static final String A_SIGNING_PRIVATE_KEY = "<a_real_private_key>";

    @Test
    public void itShouldBuildAnAuthenticationClient() throws IOException {
        //Given
        var trueLayerClient = TrueLayerClient.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_CLIENT_SECRET)
                .build();

        //When
        var authClient = trueLayerClient.Auth();

        //Then
        Assertions.assertNotNull(trueLayerClient.Auth());
    }
}
