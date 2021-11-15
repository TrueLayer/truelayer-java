package truelayer.java;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TrueLayerClientTests {

    public static final String A_CLIENT_ID = "<a_client_id>";
    public static final String A_CLIENT_SECRET = "<a_client_secret>";

    @Disabled
    @Test
    @DisplayName("It should yield an authentication client")
    @SneakyThrows
    public void itShouldBuildAnAuthenticationClient() {
        //Given
       var trueLayerClient = TrueLayerClient.builder()
                .clientCredentialsOptions(ClientCredentialsOptions.builder().clientId(A_CLIENT_ID).clientSecret(A_CLIENT_SECRET).build())
                .build();

        //When
        var token = trueLayerClient.auth().getOauthToken(List.of("paydirect"));

        //Then
        Assertions.assertNotNull(token);
    }

    @Disabled
    @Test
    @DisplayName("It should yield a payment client")
    @SneakyThrows
    public void itShouldBuildAPaymentClient() {
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
