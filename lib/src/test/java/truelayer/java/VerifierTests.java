package truelayer.java;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import truelayer.java.signing.Signer;
import truelayer.java.signing.Verifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VerifierTests {

    private static final String A_SIGNATURE = "eyJhbGciOiJFUzUxMiIsImtpZCI6ImFfa2V5X2lkIiwidGxfdmVyc2lvbiI6IjIiLCJ0bF9oZWFkZXJzIjoiSWRlbXBvdGVuY3ktS2V5In0=..Aam_czHB89OBWZux0BVYwEIrhoZFfFYJFJoX4T0WvGAO79LWdbLs472HzfWVhQzGstPOr3gMWSQxL682baocWmbkAcOVY7oGOvtM7DbTJ40SFUolEhP7KCJFzcLqxViBAgaIEOw-bFogHL3BirrFJcobi_xS2m_rT2TDlaBm5wby0NYG";
    private static final String A_PAYLOAD = "{\"foo\":\"bar\"}";
    private static final String A_KEY_ID = "a_key_id";
    private static final String A_METHOD = "POST";
    private static final String A_PATH = "/test";
    private static final Map<String, String> headers = Map.of("Idempotency-Key", "idemp_key");

    @Test
    public void VerifierShouldVerifyStaticSignature() throws JOSEException, IOException, ParseException {
        var verifierBuilder = new Verifier.Builder(A_KEY_ID, A_SIGNATURE, Files.readAllBytes(Path.of("src/test/resources/ec512-public-key.pem")))
                .addHttpMethod(A_METHOD)
                .addPath(A_PATH)
                .addBody(A_PAYLOAD);

        headers.forEach(verifierBuilder::addHeader);
        assertTrue(verifierBuilder.verify());
    }

    @Test
    public void VerifierShouldVerifyGeneratedSignature() throws JOSEException, IOException, ParseException {
        var signatureBuilder = new Signer.Builder(A_KEY_ID, Files.readAllBytes(Path.of("src/test/resources/ec512-private-key.pem")))
                .addHttpMethod(A_METHOD)
                .addPath(A_PATH)
                .addBody(A_PAYLOAD);
        headers.forEach(signatureBuilder::addHeader);
        var signature = signatureBuilder.getSignature();

        var verifierBuilder = new Verifier.Builder(A_KEY_ID, signature, Files.readAllBytes(Path.of("src/test/resources/ec512-public-key.pem")))
                .addHttpMethod(A_METHOD)
                .addPath(A_PATH)
                .addBody(A_PAYLOAD);

        headers.forEach(verifierBuilder::addHeader);
        assertTrue(verifierBuilder.verify());
    }

}
