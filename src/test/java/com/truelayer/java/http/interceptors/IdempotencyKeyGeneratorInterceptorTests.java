package com.truelayer.java.http.interceptors;

import static com.truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.TestUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IdempotencyKeyGeneratorInterceptorTests extends BaseInterceptorTests {

    @Test
    @DisplayName("It should add an Idempotency-Key header")
    public void shouldAddAIdempotencyKeyHeader() {
        arrangeRequest(new Request.Builder()
                .url("http://localhost")
                .post(RequestBody.create(new byte[] {}))
                .build());

        intercept();

        verifyThat(
                request -> assertTrue(request.header(IDEMPOTENCY_KEY).matches(TestUtils.UUID_REGEX_PATTERN.pattern())));
    }

    @Test
    @DisplayName("It should not add an Idempotency-Key header if already set")
    public void shouldNotAddAIdempotencyKeyHeaderIfAlreadySet() {
        String customIdempotencyKey = "a-ky";
        arrangeRequest(new Request.Builder()
                .url("http://localhost")
                .header(IDEMPOTENCY_KEY, customIdempotencyKey)
                .post(RequestBody.create(new byte[] {}))
                .build());

        intercept();

        verifyThat(request -> assertEquals(customIdempotencyKey, request.header(IDEMPOTENCY_KEY)));
    }

    @Override
    protected Interceptor getInterceptor() {
        return new IdempotencyKeyGeneratorInterceptor();
    }
}
