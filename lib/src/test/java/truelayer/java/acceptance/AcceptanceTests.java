package truelayer.java.acceptance;

import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import truelayer.java.ClientCredentials;
import truelayer.java.Environment;
import truelayer.java.SigningOptions;
import truelayer.java.TrueLayerClient;

@Tag("acceptance")
public abstract class AcceptanceTests {

    protected static TrueLayerClient tlClient;

    @BeforeAll
    public static void setup() {
        tlClient = TrueLayerClient.New()
                .environment(Environment.development())
                .clientCredentials(ClientCredentials.builder()
                        .clientId(System.getenv("TL_CLIENT_ID"))
                        .clientSecret(System.getenv("TL_CLIENT_SECRET"))
                        .build())
                .signingOptions(SigningOptions.builder()
                        .keyId(System.getenv("TL_SIGNING_KEY_ID"))
                        .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY").getBytes(StandardCharsets.UTF_8))
                        .build())
                .withHttpLogs()
                .build();
    }
}
