package com.truelayer.java.http.interceptors;

import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignatureGeneratorInterceptorTests extends BaseInterceptorTests {

    private static final String A_PAYLOAD = "{\"foo\":\"bar\"}";

    @Override
    protected Interceptor getInterceptor() {
        return new SignatureGeneratorInterceptor(TestUtils.getSigningOptions());
    }

    @Test
    @DisplayName("It should not add a Tl-Signature header on a GET request")
    public void shouldNotAddATlSignatureHeaderOnGet() {
        arrangeRequest(new Request.Builder().url("http://localhost").get().build());

        intercept();

        verifyThat(interceptedRequest -> assertNull(interceptedRequest.header(Constants.HeaderNames.TL_SIGNATURE)));
    }

    @Test
    @DisplayName("It should not add a Tl-Signature header if already set")
    public void shouldNotAddATlSignatureHeaderIfAlreadySet() {
        fail("todo");
    }

    @Test
    @SneakyThrows
    @DisplayName("It should add a Tl-Signature header on a GET request")
    public void shouldAddATlSignatureHeaderOnPost() {
        arrangeRequest(new Request.Builder()
                .url("http://localhost")
                .header(Constants.HeaderNames.IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .post(RequestBody.create(MediaType.get("application/json"), A_PAYLOAD.getBytes(StandardCharsets.UTF_8)))
                .build());

        intercept();

        verifyThat(request ->
                assertFalse(request.header(Constants.HeaderNames.TL_SIGNATURE).isEmpty()));
    }
}
