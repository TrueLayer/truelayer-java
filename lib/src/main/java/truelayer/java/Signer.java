package truelayer.java;

import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import lombok.val;

import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.ECPrivateKey;
import java.util.Map;

public class Signer {

    //todo remove this before open sourcing the
    private static final String CONSOLE_ID = "7695796e-e718-457d-845b-4a6be00ca454";

    private static final String PAYLOAD = "{\n" +
            "\"amount_in_minor\": 1,\n" +
            "\"currency\": \"GBP\",\n" +
            "\"payment_method\": {\n" +
            "\"type\": \"bank_transfer\",\n" +
            "\"statement_reference\": \"string\",\n" +
            "\"provider_filter\": {}\n" +
            "},\n" +
            "\"beneficiary\": {\n" +
            "\"type\": \"merchant_account\",\n" +
            "\"id\": \"string\",\n" +
            "\"name\": \"string\"\n" +
            "},\n" +
            "\"user\": {\n" +
            "\"type\": \"new\",\n" +
            "\"name\": \"Remi Terr\",\n" +
            "\"email\": \"remi.terr@aol.com\",\n" +
            "\"phone\": \"+44777777777\"\n" +
            "}\n" +
            "}";

    public String generateJwsSignature() throws JOSEException {

        // Generate an EC key pair
        ECKey ecJWK = new ECKeyGenerator(Curve.P_521)
                .keyID(CONSOLE_ID)
                .generate();

        ECKey ecPublicJWK = ecJWK.toPublicJWK();

        // Create the EC signer
        JWSSigner signer = new ECDSASigner(ecJWK);

        Gson gson = new Gson();
        String jsonString = gson.toJson(PAYLOAD);

        // Creates the JWS object with payload
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES512)
                        .keyID(ecJWK.getKeyID())
                        .customParam("tl_version", "2")
                        //TODO check this
                        .customParam("tl_headers", "Idempotency-Key")
                        .build(),
                new Payload(jsonString));

        // Compute the EC signature
        jwsObject.sign(signer);

        // Serialize the JWS to compact form
        return jwsObject.serialize();





    }


//    private String buildPayload(Map<String, String> headers, String method, String path, byte[] body) {
//
//        val entries = headers.e   entries
//        val headersBytes: ByteArray = entries.fold(ByteArray(0)) { acc, entry ->
//                acc + entry.key.name.toByteArray() +
//                        ": ".toByteArray() +
//                        entry.value.toByteArray() +
//                        "\n".toByteArray()
//        }
//
//        val payload = method.uppercase().toByteArray() +
//                " ".toByteArray() +
//                path.toByteArray() +
//                "\n".toByteArray() +
//                headersBytes +
//                body
//
//        return payload.toUrlBase64()
//    }
//
//
//    //we need to parse the .pem cert file
//    private ECPrivateKey parseEcPrivateKey() {
//        Security.addProvider(BouncyCastleProvider());
//        val pemObject = PemReader(InputStreamReader(ByteArrayInputStream(privateKey))).readPemObject();
//
//        val kf = KeyFactory.getInstance("EC");
//        val asn1PrivKey = org.bouncycastle.asn1.sec.ECPrivateKey.getInstance(pemObject.content);
//        val parameterSpec = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec("secp521r1");
//        val keySpec = ECPrivateKeySpec(asn1PrivKey.key, parameterSpec);
//
//        return kf.generatePrivate(keySpec) as ECPrivateKey;
//    }
}