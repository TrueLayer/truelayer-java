package truelayer.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.exceptions.AuthenticationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Disabled
public class TrueLayerClientTests {

    public static final String A_CLIENT_ID = "<a_client_id>";
    public static final String A_CLIENT_SECRET = "<a_client_secret>";

    @Test
    @DisplayName("It should yield an authentication client")
    public void itShouldBuildAnAuthenticationClient() throws AuthenticationException, IOException {
        //Given
       var trueLayerClient = TrueLayerClient.builder()
                .clientCredentialsOptions(ClientCredentialsOptions.builder().clientId(A_CLIENT_ID).clientSecret(A_CLIENT_SECRET).build())
                .build();

        //When
        var token = trueLayerClient.auth().getOauthToken(List.of("paydirect"));

        //Then
        Assertions.assertNotNull(token);
    }

    @Test
    @DisplayName("It should yield a payment client")
    public void itShouldBuildAPaymentClient() throws IOException {
        //Given
       /* var trueLayerClient = TrueLayerClient.builder()
                .clientId(A_CLIENT_ID)
                .clientSecret(A_CLIENT_SECRET)
                .signingOptions(SigningOptions.builder()
                        .keyId("my-key-id")
                        .privateKey(Files.readAllBytes(Path.of("src/test/resources/ec512-private-key.pem")))
                        .build())
                .build();

        //When
        var payments = trueLayerClient.payments();

        //Then
        Assertions.assertNotNull(payments);*/
    }
}
