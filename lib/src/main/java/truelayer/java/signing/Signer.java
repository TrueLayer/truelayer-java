package truelayer.java.signing;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Signer {

    private Map<String, String> headers;
    private byte[] privateKey;
    private String keyId;
    private String body;
    private String httpMethod = "POST";
    private String path;

    public static class Builder {
        private final Signer signer;

        public Builder(String keyId, byte[] privateKey) {
            signer = new Signer();
            signer.init(keyId, privateKey);
        }

        public Builder addHeader(String key, String value) {
            signer.addHeader(key, value);
            return this;
        }

        public Builder addBody(String body) {
            signer.addBody(body);
            return this;
        }

        public Builder addHttpMethod(String httpMethod) {
            signer.addHttpMethod(httpMethod);
            return this;
        }

        public Builder addPath(String path) {
            signer.addPath(path);
            return this;
        }

        public String getSignature() throws IOException, JOSEException {
            return signer.sign();
        }
    }

    private void init(String keyId, byte[] privateKey) {
        this.keyId = keyId;
        this.privateKey = privateKey;
    }

    private void addHeader(String key, String value) {
        if(headers == null)
            headers = new HashMap<>();
        headers.put(key, value);
    }

    private void addBody(String body) {
        this.body = body;
    }

    private void addHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    private void addPath(String path) {
        this.path = path;
    }

    private String parseHeaders() {
        return headers.keySet().stream()
                .map(key -> key + ": " + headers.get(key))
                .collect(Collectors.joining("\n"));
    }

    private String buildPayload() {
        var builder = new StringBuilder(httpMethod + " " + path);
        if(headers != null)
            builder.append("\n")
                    .append(parseHeaders());

        if(body != null)
            builder.append("\n")
                    .append(body);

        return builder.toString();
    }


    private String sign() throws JOSEException, IOException {
        var privateKeyString = new String(privateKey);

        var ecJWK = ECKey.parseFromPEMEncodedObjects(privateKeyString);

        // Create the EC signer
        JWSSigner signer = new ECDSASigner((ECKey) ecJWK);

        // Creates the JWS object with payload
        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES512)
                        .keyID(keyId)
                        .customParam("tl_version", "2")
                        .customParam("tl_headers", String.join(",", headers.keySet()))
                        .build(),
                new Payload(buildPayload()));

        // Compute the EC signature
        jwsObject.sign(signer);

        // Serialize the JWS to compact form
        return jwsObject.serialize();
    }

}
