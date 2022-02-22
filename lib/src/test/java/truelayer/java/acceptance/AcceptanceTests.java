package truelayer.java.acceptance;

import java.net.URI;
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
        /*tlClient = TrueLayerClient.New()
        .environment(Environment.sandbox())
        .clientCredentials(ClientCredentials.builder()
                .clientId(System.getenv("TL_CLIENT_ID"))
                .clientSecret(System.getenv("TL_CLIENT_SECRET"))
                .build())
        .signingOptions(SigningOptions.builder()
                .keyId(System.getenv("TL_SIGNING_KEY_ID"))
                .privateKey(System.getenv("TL_SIGNING_PRIVATE_KEY").getBytes(StandardCharsets.UTF_8))
                .build())
        .withHttpLogs()
        .build();*/

        tlClient = TrueLayerClient.New()
                .environment(Environment.custom(
                        URI.create("https://auth.t7r.dev"),
                        URI.create("https://api.t7r.dev"),
                        URI.create("https://payment.t7r.dev")))
                .clientCredentials(ClientCredentials.builder()
                        .clientId("alessandrov3-94e954")
                        .clientSecret("220dea8d-eba9-491a-9c28-7ba5eba8732e")
                        .build())
                .signingOptions(SigningOptions.builder()
                        .keyId("1f2e5401-6d04-439d-ad10-cb86750e31c0")
                        .privateKey(
                                "\n-----BEGIN EC PRIVATE KEY-----\nMIHbAgEBBEGWCI86v/uLdKtWnZ9lSWRCf+1INYOav+n7W33GipnfnYjySx1xfRss\nDgyXMEelkDxKYKvv5SChLnW45xC7y0gOa6AHBgUrgQQAI6GBiQOBhgAEAElaHHWs\nrNkQH73CULswCNL/p5VL4rfo1KOgX3v+FFR8RL7zZNiut16Z3PlN/Pmh1qt2mGjv\nsKDe4loseEb8DJyEAOgsWKPq/lpUvk1v4UOeV\nsXTTWNLr6jjePFAnbg1JCKQ3TPD\nH/4PJWxgdkCXyMKLM0yAnweiaDVHkMH5BiKeCIrW\n-----END EC PRIVATE KEY-----\n"
                                        .getBytes(StandardCharsets.UTF_8))
                        .build())
                .withHttpLogs()
                .build();
    }
}
