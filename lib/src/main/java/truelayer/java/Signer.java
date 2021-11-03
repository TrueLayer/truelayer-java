package truelayer.java;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Signer {

    private static final String PRIVATE_KEY_FILE_PATH = "/Users/luca/Work/paydirect-signing-examples/ec512-private-key.pem";
    private static final String PUBLIC_KEY_FILE_PATH = "/Users/luca/Work/paydirect-signing-examples/ec512-public-key.pem";

    public static String getJwsSignature(String payload) throws JOSEException, IOException {
        var privateKey = Files.readString(Path.of(PRIVATE_KEY_FILE_PATH));

        var ecJWK = ECKey.parseFromPEMEncodedObjects(privateKey);
        // Generate an EC key pair
        /*
        ECKey ecJWK = new ECKeyGenerator(Curve.P_521)
                .keyID("123")
                .generate();
        ECKey ecPublicJWK = ecJWK.toPublicJWK();
*/

        // Create the EC signer
        JWSSigner signer = new ECDSASigner((ECKey) ecJWK);

        // Creates the JWS object with payload
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES512)
                        .keyID(ecJWK.getKeyID())
                        .customParam("tl_version", "2")
                        .customParam("tl_headers", "Idempotency-Key")
                        .build(),
                new Payload(payload));

        // Compute the EC signature
        jwsObject.sign(signer);

        // Serialize the JWS to compact form
        return jwsObject.serialize();
    }

}
