package com.truelayer.java.http.interceptors.logging;

import static com.truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;
import static com.truelayer.java.http.interceptors.logging.HttpLogPrefix.INCOMING;
import static com.truelayer.java.http.interceptors.logging.HttpLogPrefix.OUTGOING;

import com.truelayer.java.http.LoggerFactory;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.tinylog.TaggedLogger;
import org.tinylog.ThreadContext;

public class HttpLoggingInterceptor implements Interceptor {
    protected static final String MESSAGE_FORMAT = "{} {} {} headers={}";
    private final TaggedLogger logger;
    private final SensitiveHeaderGuard sensitiveHeaderGuard;

    HttpLoggingInterceptor(TaggedLogger logger, SensitiveHeaderGuard sensitiveHeaderGuard) {
        this.logger = logger;
        this.sensitiveHeaderGuard = sensitiveHeaderGuard;
    }

    public static HttpLoggingInterceptor New() {
        return new HttpLoggingInterceptor(LoggerFactory.NewHttpLogger(), new SensitiveHeaderGuard());
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        String idempotencyKey = request.header(IDEMPOTENCY_KEY);

        ThreadContext.put(IDEMPOTENCY_KEY, idempotencyKey);
        logger.trace(
                MESSAGE_FORMAT,
                OUTGOING,
                request.method(),
                request.url(),
                sensitiveHeaderGuard.getSanitizedHeaders(request.headers()));

        Response response = chain.proceed(request);
        logger.trace(
                MESSAGE_FORMAT,
                INCOMING,
                response.code(),
                request.method(),
                request.url(),
                sensitiveHeaderGuard.getSanitizedHeaders(request.headers()));

        ThreadContext.clear();
        return response;
    }
}
