package com.truelayer.java.http.interceptors.logging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.truelayer.java.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.internal.http2.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SensitiveHeaderGuardTests {

    @Test
    @DisplayName("It should return an unmodifiable list")
    public void shouldReturnEmptyList() {
        SensitiveHeaderGuard sut = new SensitiveHeaderGuard();
        Headers headers = Headers.of(new HashMap<>());

        List<Header> sanitizedHeaders = sut.getSanitizedHeaders(headers);

        assertThrows(UnsupportedOperationException.class, () -> sanitizedHeaders.add(new Header("foo", "bar")));
        assertTrue(sanitizedHeaders.isEmpty());
    }

    @Test
    @DisplayName("It should return an unmodifiable list with masked values")
    public void shouldMaskSensitiveHeaders() {
        SensitiveHeaderGuard sut = new SensitiveHeaderGuard();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(Constants.HeaderNames.AUTHORIZATION, "Bearer a-very-sensitive-header");
        headersMap.put(Constants.HeaderNames.COOKIE, "a-cookie");
        headersMap.put(Constants.HeaderNames.TL_AGENT, "a-user-agent");
        Headers headers = Headers.of(headersMap);

        List<Header> sanitizedHeaders = sut.getSanitizedHeaders(headers);

        assertThrows(UnsupportedOperationException.class, () -> sanitizedHeaders.add(new Header("foo", "bar")));
        sanitizedHeaders.forEach(h -> {
            if (sut.isSensitiveHeader(h.name.toString())) {
                Assertions.assertEquals(SensitiveHeaderGuard.SENSITIVE_HEADER_MASK, h.value.toString());
            }
        });
    }
}
