package truelayer.java.http.interceptors.logging;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.Constants.HeaderNames.*;
import static truelayer.java.http.interceptors.logging.SensitiveHeaderGuard.SENSITIVE_HEADER_MASK;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Headers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.http.entities.Header;

class SensitiveHeaderGuardTests {

    @Test
    @DisplayName("it should mask sensitive headers")
    public void shouldMaskSensitiveHeaders() {
        SensitiveHeaderGuard sut = new SensitiveHeaderGuard();
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(AUTHORIZATION, "Bearer a-very-sensitive-header");
        headersMap.put(COOKIE, "a-cookie");
        headersMap.put(USER_AGENT, "a-user-agent");
        Headers headers = Headers.of(headersMap);

        List<Header> sanitizedHeaders = sut.getSanitizedHeaders(headers);

        sanitizedHeaders.forEach(h -> {
            if (sut.isSensitiveHeader(h.name())) {
                assertEquals(SENSITIVE_HEADER_MASK, h.value());
            }
        });
    }
}
