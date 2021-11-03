package truelayer.java;

import com.nimbusds.jose.JOSEException;
import org.junit.Test;

import java.io.IOException;

public class SignerTests {

    @Test
    public void SignerShouldCreateValidSignature() throws JOSEException, IOException {
        var payload = "{\"foo\":\"bar\"}";
        var signature = Signer.getJwsSignature(payload);
        System.out.println(signature);
    }
}
