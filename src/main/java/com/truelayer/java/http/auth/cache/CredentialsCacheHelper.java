package com.truelayer.java.http.auth.cache;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;

import com.truelayer.java.TrueLayerException;
import com.truelayer.java.entities.RequestScopes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CredentialsCacheHelper {
    private static final String CACHE_KEY_PREFIX = "tl-auth-token";
    private static final String SCOPES_DELIMITER = ",";

    public static String buildKey(String clientId, RequestScopes requestScopes) {
        if (isEmpty(clientId) || isEmpty(requestScopes) || isEmpty(requestScopes.getScopes())) {
            throw new TrueLayerException("Invalid client id or request scopes provided");
        }

        List<String> scopes = new ArrayList<>(requestScopes.getScopes());

        // Use natural ordering to make ordering not significant
        Collections.sort(scopes);

        byte[] md5InBytes = digest(String.join(SCOPES_DELIMITER, scopes).getBytes(UTF_8));
        return MessageFormat.format("{0}:{1}:{2}", CACHE_KEY_PREFIX, clientId, bytesToHex(md5InBytes));
    }

    private static byte[] digest(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new TrueLayerException("Hashing algorithm is not available", e);
        }
        return md.digest(input);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
