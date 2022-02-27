package com.truelayer.java.http.interceptors.logging;

import com.truelayer.java.Constants;
import com.truelayer.java.http.entities.Header;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Headers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SensitiveHeaderGuardTests {

    @Test
    @DisplayName("it should mask sensitive headers")
    public void shouldMaskSensitiveHeaders() {
        SensitiveHeaderGuard sut = new SensitiveHeaderGuard();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(Constants.HeaderNames.AUTHORIZATION, "Bearer a-very-sensitive-header");
        headersMap.put(Constants.HeaderNames.COOKIE, "a-cookie");
        headersMap.put(Constants.HeaderNames.USER_AGENT, "a-user-agent");
        Headers headers = Headers.of(headersMap);

        List<Header> sanitizedHeaders = sut.getSanitizedHeaders(headers);

        sanitizedHeaders.forEach(h -> {
            if (sut.isSensitiveHeader(h.name())) {
                Assertions.assertEquals(SensitiveHeaderGuard.SENSITIVE_HEADER_MASK, h.value());
            }
        });
    }
}
