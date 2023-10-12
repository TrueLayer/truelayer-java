package com.truelayer.java.http.mappers;

import static com.truelayer.java.Constants.HeaderNames.*;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.http.entities.Headers;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HeadersMapperTests {

    @Test
    @DisplayName("it should convert Headers object to an unmodifiable map")
    public void shouldConvertHeadersObjectToMap() {
        Headers headers = Headers.builder()
                .xForwardedFor("1.2.3.4")
                .signature("signed!")
                .idempotencyKey("very-unique-key")
                .build();

        Map<String, String> headersMap = HeadersMapper.toMap(headers);

        assertThrows(UnsupportedOperationException.class, () -> headersMap.put("foo", "bar"));
        assertEquals(headers.getXForwardedFor(), headersMap.get(X_FORWARDED_FOR));
        assertEquals(headers.getXDeviceUserAgent(), headersMap.get(X_DEVICE_USER_AGENT));
        assertEquals(headers.getSignature(), headersMap.get(TL_SIGNATURE));
        assertEquals(headers.getIdempotencyKey(), headersMap.get(IDEMPOTENCY_KEY));
    }
}
