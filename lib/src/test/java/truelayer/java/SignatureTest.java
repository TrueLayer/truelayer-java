/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package truelayer.java;

import org.junit.jupiter.api.Test;
import truelayer.signing.Signer;
import truelayer.signing.Verifier;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Test class to test the imported kotlin library
 */
public class SignatureTest {

    static String KID = "45fc75cf-5649-4134-84b3-192c2c78e990";

    @Test
    public void fullSignature() throws IOException {
        //place your certificates in the test/resources folder
        byte[] privateKey = Files.readAllBytes(Path.of("/Users/giulio.leso/Desktop/ec512-private-key.pem"));
        byte[] publicKey = Files.readAllBytes(Path.of("/Users/giulio.leso/Desktop/ec512-public-key.pem"));

        byte[] body = "{\"currency\":\"GBP\",\"max_amount_in_minor\":5000000}".getBytes(StandardCharsets.UTF_8);
        String idempotencyKey = "idemp-2076717c-9005-4811-a321-9e0787fa0382";
        String path = "/merchant_accounts/a61acaef-ee05-4077-92f3-25543a11bd8d/sweeping";

        String tlSignature = Signer.from(KID, privateKey)
                .header("Idempotency-Key", idempotencyKey)
                .method("post")
                .path(path)
                .body(body)
                .sign();

        boolean verified = Verifier.from(publicKey)
                .method("POST")
                .path(path)
                .header("X-Whatever", "aoitbeh")
                .header("Idempotency-Key", idempotencyKey)
                .body(body)
                .requiredHeader("Idempotency-Key")
                .verify(tlSignature);

        assertTrue(verified);
    }
}
