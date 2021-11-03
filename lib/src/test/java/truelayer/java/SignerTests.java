package truelayer.java;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SignerTests {

    @Test
    public void SignerShouldCreateValidSignature() throws JOSEException, IOException {
        var payload = "{\"foo\":\"bar\"}";
        var signature = new Signer.Builder("a_key_id", Files.readAllBytes(Path.of("/Users/luca/Work/paydirect-signing-examples/ec512-private-key.pem")))
                .addHttpMethod("POST")
                .addHeader("Idempotency-Key", "idemp_key")
                .addPath("/test")
                .addBody(payload)
                .getSignature();
        System.out.println(signature);
    }
}
