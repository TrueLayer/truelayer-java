package com.truelayer.java.http.interceptors.logging;

import static com.truelayer.java.http.interceptors.logging.HttpLogPrefix.INCOMING;
import static com.truelayer.java.http.interceptors.logging.HttpLogPrefix.OUTGOING;

import com.truelayer.java.http.entities.Header;
import com.truelayer.java.http.interceptors.logging.HttpLogMessage.HttpLogMessageBuilder;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HttpLogMessageBuilderTests {

    private static final String AN_IDEMPOTENCY_KEY = UUID.randomUUID().toString();
    private static final String A_URL = "http://localhost";
    private static final String A_METHOD = "POST";
    private static final int CODE = 201;
    private static final List<Header> HEADERS = Collections.singletonList(new Header("header_name", "header_value"));

    @Test
    @DisplayName("It should build a HTTP request log")
    public void itShouldBuildARequestTrace() {
        HttpLogMessageBuilder sut = HttpLogMessage.builder();

        String message = sut.prefix(OUTGOING)
                .idempotencyKey(AN_IDEMPOTENCY_KEY)
                .method(A_METHOD)
                .url(A_URL)
                .headers(HEADERS)
                .build();

        String expectedMessage =
                String.format("[HTTP|%s] %s %s %s headers=%s", AN_IDEMPOTENCY_KEY, OUTGOING, A_METHOD, A_URL, HEADERS);
        Assertions.assertEquals(expectedMessage, message);
    }

    @Test
    @DisplayName("It should build a HTTP response log")
    public void itShouldBuildAResponseTrace() {
        HttpLogMessageBuilder sut = HttpLogMessage.builder();

        String message = sut.prefix(INCOMING)
                .idempotencyKey(AN_IDEMPOTENCY_KEY)
                .code(CODE)
                .url(A_URL)
                .headers(HEADERS)
                .build();

        String expectedMessage =
                String.format("[HTTP|%s] %s %s %s headers=%s", AN_IDEMPOTENCY_KEY, INCOMING, CODE, A_URL, HEADERS);
        Assertions.assertEquals(expectedMessage, message);
    }
}
