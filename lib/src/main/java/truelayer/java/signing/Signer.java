package truelayer.java.signing;

import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jose.util.JSONObjectUtils;

import java.text.ParseException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Signer {

    private Map<String, String> headers;
    private byte[] privateKey;
    private String keyId;
    private String body;
    private String httpMethod = "POST";
    private String path;

    private void init(String keyId, byte[] privateKey) {
        this.keyId = keyId;
        this.privateKey = privateKey;
    }

    private void addHeader(String key, String value) {
        if (headers == null)
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

        public String getSignature() throws JOSEException, ParseException {
            return signer.sign();
        }
    }


    private String sign() throws JOSEException, ParseException {
        var privateKeyString = new String(privateKey);

        var ecJWK = ECKey.parseFromPEMEncodedObjects(privateKeyString);

        // Create the EC signer
        JWSSigner signer = new ECDSASigner((ECKey) ecJWK);

        //we need to keep headers in this specific order
        var signatureHeaders = new LinkedHashMap<String, String>();
        signatureHeaders.put("alg", "ES512");
        signatureHeaders.put("kid", keyId);
        signatureHeaders.put("tl_version", "2");
        signatureHeaders.put("tl_headers", String.join(",", headers.keySet()));

        var signatureHeadersJson = JSONObjectUtils.toJSONString(signatureHeaders);
        var signatureHeaderBase64 = Base64.getUrlEncoder().encodeToString(signatureHeadersJson.getBytes());

        // Creates the JWS object with payload
        JWSObject jwsObject = new JWSObject(
                JWSHeader.parse(new Base64URL(signatureHeaderBase64)),
                new Payload(new Base64URL(Utils.buildPayload(httpMethod, path, body, headers))));

        // Compute the EC signature
        jwsObject.sign(signer);

        // Serialize the JWS to compact form
        return jwsObject.serialize(true);
    }

}
