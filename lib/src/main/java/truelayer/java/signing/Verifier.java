package truelayer.java.signing;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.util.Base64URL;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Verifier {

    private Map<String, String> headers;
    private byte[] publicKey;
    private String keyId;
    private String body;
    private String httpMethod = "POST";
    private String path;
    private String signature;

    private void init(String keyId, String signature, byte[] publicKey) {
        this.keyId = keyId;
        this.signature = signature;
        this.publicKey = publicKey;
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


    public static class Builder {
        private final Verifier verifier;

        public Builder(String keyId, String signature, byte[] privateKey) {
            verifier = new Verifier();
            verifier.init(keyId, signature, privateKey);
        }

        public Builder addHeader(String key, String value) {
            verifier.addHeader(key, value);
            return this;
        }

        public Builder addBody(String body) {
            verifier.addBody(body);
            return this;
        }

        public Builder addHttpMethod(String httpMethod) {
            verifier.addHttpMethod(httpMethod);
            return this;
        }

        public Builder addPath(String path) {
            verifier.addPath(path);
            return this;
        }

        public boolean verify() throws JOSEException, ParseException {
            return verifier.verify();
        }
    }

    private boolean verify() throws ParseException, JOSEException {
        return verifySignatureHeaders() && verifyPayload();
    }

    private boolean verifyPayload() throws JOSEException, ParseException {
        var publicKeyString = new String(publicKey);
        var ecJWK = ECKey.parseFromPEMEncodedObjects(publicKeyString);
        var payload = new Payload(new Base64URL(Utils.buildPayload(httpMethod, path, body, headers)));
        return JWSObject.parse(signature, payload).verify(new ECDSAVerifier((ECKey) ecJWK));
    }

    private boolean verifySignatureHeaders() throws ParseException {
        var requiredHeaderNames = headers.keySet();
        var jwsHeaders = JWSHeader.parse(JOSEObject.split(signature)[0]);

        var keyIdParam = jwsHeaders.getKeyID();
        if(keyIdParam == null || !keyIdParam.equalsIgnoreCase(keyId))
            return false;

        if(!jwsHeaders.getAlgorithm().equals(JWSAlgorithm.ES512))
            return false;

        var tlVersionParam = jwsHeaders.getCustomParam("tl_version");
        if(tlVersionParam == null || !tlVersionParam.toString().equalsIgnoreCase("2"))
            return false;

        var tlHeadersParam = jwsHeaders.getCustomParam("tl_headers");
        if(tlHeadersParam == null)
            return false;

        var tlHeadersList = Arrays.stream(tlHeadersParam.toString().split(",")).collect(toList());
        return tlHeadersList.containsAll(requiredHeaderNames);
    }
}
