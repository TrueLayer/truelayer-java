package truelayer.java.http.interceptors.logging;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.common.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.common.Constants.HeaderNames.COOKIE;

import java.util.Map;
import okhttp3.Headers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SensitiveHeaderGuardTests {

    @Test
    @DisplayName("it should mask sensitive headers")
    public void shouldMaskSensitiveHeaders() {
        var sut = new SensitiveHeaderGuard();
        Map<String, String> headersMap =
                Map.of(AUTHORIZATION, "Bearer a-very-sensitive-header", COOKIE, "a-cookie", "User-Agent", "whatever");
        Headers headers = Headers.of(headersMap);

        var sanitizedHeaders = sut.getSanitizedHeaders(headers);

        sanitizedHeaders.forEach(h -> {
            var headerName = h.substring(0, h.indexOf("="));
            if (sut.isSensitiveHeader(headerName)) {
                assertTrue(h.endsWith("=" + SensitiveHeaderGuard.SENSITIVE_HEADER_MASK));
            }
        });
    }
}
