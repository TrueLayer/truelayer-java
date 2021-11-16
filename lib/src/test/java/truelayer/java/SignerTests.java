package truelayer.java;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import truelayer.java.signing.Signer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//todo review to exclude fs references
public class SignerTests {

    private static final String A_PAYLOAD = "{\"foo\":\"bar\"}";
    private static final String A_KEY_ID = "a_key_id";
    private static final String A_METHOD = "POST";
    private static final String A_PATH = "/test";
    private static final Map<String, String> headers = Map.of("Idempotency-Key", "idemp_key");

    @Test
    @Disabled
    public void SignerShouldCreateValidSignature() throws JOSEException, IOException, ParseException {
        var signatureBuilder = new Signer.Builder(A_KEY_ID, Files.readAllBytes(Path.of("src/test/resources/ec512-private-key.pem")))
                .addHttpMethod(A_METHOD)
                .addPath(A_PATH)
                .addBody(A_PAYLOAD);
        headers.forEach(signatureBuilder::addHeader);
        var signature = signatureBuilder.getSignature();
        System.out.println(signature);
        assertNotNull(signature);
    }
}
