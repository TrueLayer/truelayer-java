package truelayer.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TrueLayerClientTests {

    public static final String A_CLIENT_ID = "<a_client_id>";
    public static final String A_CLIENT_SECRET = "<a_real_secret>";

    @Test
    @DisplayName("It should yield an authentication client")
    public void itShouldBuildAnAuthenticationClient() throws IOException {
        //Given
        var trueLayerClient = TrueLayerClient.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_CLIENT_SECRET)
                .build();

        //When
        var auth = trueLayerClient.auth();

        //Then
        Assertions.assertNotNull(auth);
    }

    @Test
    @DisplayName("It should yield a payment client")
    public void itShouldBuildAPaymentClient() throws IOException {
        //Given
        var trueLayerClient = TrueLayerClient.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_CLIENT_SECRET)
                .signingOptions(SigningOptions.builder()
                        .keyId("my-key-id")
                        .privateKey("my-private-key")
                        .build())
                .build();

        //When
        var payments = trueLayerClient.payments();

        //Then
        Assertions.assertNotNull(payments);
    }
}
