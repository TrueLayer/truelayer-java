package com.truelayer.java.http.interceptors.logging;

import static com.truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;
import static com.truelayer.java.http.interceptors.logging.HttpLogPrefix.INCOMING;
import static com.truelayer.java.http.interceptors.logging.HttpLogPrefix.OUTGOING;

import java.io.IOException;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@RequiredArgsConstructor
public class HttpLoggingInterceptor implements Interceptor {
    private final Consumer<String> logMessageConsumer;
    private final SensitiveHeaderGuard sensitiveHeaderGuard;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String idempotencyKey = request.header(IDEMPOTENCY_KEY);

        String requestTrace = HttpLogMessage.builder()
                .idempotencyKey(idempotencyKey)
                .prefix(OUTGOING)
                .method(request.method())
                .url(request.url().toString())
                .headers(sensitiveHeaderGuard.getSanitizedHeaders(request.headers()))
                .build();
        logMessageConsumer.accept(requestTrace);

        Response response = chain.proceed(request);

        String responseTrace = HttpLogMessage.builder()
                .idempotencyKey(idempotencyKey)
                .prefix(INCOMING)
                .code(response.code())
                .method(request.method())
                .url(request.url().toString())
                .headers(sensitiveHeaderGuard.getSanitizedHeaders(response.headers()))
                .build();
        logMessageConsumer.accept(responseTrace);

        return response;
    }
}
