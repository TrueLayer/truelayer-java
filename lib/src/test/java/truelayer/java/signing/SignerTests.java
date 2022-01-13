package truelayer.java.signing;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;

public class SignerTests {
    private static final String A_PAYLOAD = "{\"foo\":\"bar\"}";
    private static final String A_KEY_ID = "a_key_id";
    private static final String A_METHOD = "POST";
    private static final String A_PATH = "/test";
    private static final Map<String, String> headers = Map.of(IDEMPOTENCY_KEY, "idemp_key");

    @Test
    @DisplayName("It should create a valid signature")
    @SneakyThrows
    public void SignerShouldCreateValidSignature() {
        var signatureBuilder = new Signer.Builder(A_KEY_ID, TestUtils.getPrivateKey())
                .addHttpMethod(A_METHOD)
                .addPath(A_PATH)
                .addBody(A_PAYLOAD);
        headers.forEach(signatureBuilder::addHeader);
        var signature = signatureBuilder.getSignature();
        System.out.println(signature);
        assertNotNull(signature);
    }
}
