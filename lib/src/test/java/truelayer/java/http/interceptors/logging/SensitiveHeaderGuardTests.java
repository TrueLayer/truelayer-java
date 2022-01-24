package truelayer.java.http.interceptors.logging;

import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.common.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.common.Constants.HeaderNames.COOKIE;
import static truelayer.java.http.interceptors.logging.SensitiveHeaderGuard.SENSITIVE_HEADER_MASK;

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
            if (sut.isSensitiveHeader(h.name())) {
                assertEquals(SENSITIVE_HEADER_MASK, h.value());
            }
        });
    }
}
