package com.truelayer.java.http.interceptors.customheaders;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.truelayer.java.Constants;
import com.truelayer.java.http.entities.Headers;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class CustomHeadersConverter {
    static Map<String, String> toMap(Headers customHeaders) {
        Map<String, String> headersMap = new HashMap<>();

        if (isEmpty(customHeaders)) {
            return Collections.unmodifiableMap(headersMap);
        }

        String idempotencyKey = customHeaders.getIdempotencyKey();
        if (isNotEmpty(idempotencyKey)) {
            headersMap.put(Constants.HeaderNames.IDEMPOTENCY_KEY, idempotencyKey);
        }

        String signature = customHeaders.getSignature();
        if (isNotEmpty(signature)) {
            headersMap.put(Constants.HeaderNames.TL_SIGNATURE, signature);
        }

        String xForwardedFor = customHeaders.getXForwardedFor();
        if (isNotEmpty(xForwardedFor)) {
            headersMap.put(Constants.HeaderNames.X_FORWARDED_FOR, xForwardedFor);
        }

        return Collections.unmodifiableMap(headersMap);
    }
}
