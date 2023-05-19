package com.truelayer.java.http.interceptors.customheaders;

import static com.truelayer.java.Constants.HeaderNames.*;
import static org.junit.jupiter.api.Assertions.*;

import com.truelayer.java.http.entities.Headers;
import com.truelayer.java.http.interceptors.BaseInterceptorTests;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CustomHeadersInterceptorTests extends BaseInterceptorTests {

    @Test
    @DisplayName("Should stream custom headers tag to request headers")
    public void shouldStreamHeadersTagToRequestHeader() {
        Headers customHeadersTag = Headers.builder()
                .idempotencyKey("a-key")
                .signature("a-signature")
                .xForwardedFor("1.2.3.4")
                .build();
        arrangeRequest(new Request.Builder()
                .tag(Headers.class, customHeadersTag)
                .url("http://localhost")
                .get()
                .build());

        intercept();

        verifyThat(interceptedRequest -> {
            assertEquals(customHeadersTag.getIdempotencyKey(), interceptedRequest.header(IDEMPOTENCY_KEY));
            assertEquals(customHeadersTag.getSignature(), interceptedRequest.header(TL_SIGNATURE));
            assertEquals(customHeadersTag.getXForwardedFor(), interceptedRequest.header(X_FORWARDED_FOR));
        });
    }

    @Override
    protected Interceptor getInterceptor() {
        return new CustomHeadersInterceptor();
    }
}
