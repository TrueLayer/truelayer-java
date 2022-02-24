package truelayer.java.http.interceptors.logging;

import static truelayer.java.Constants.HeaderNames.IDEMPOTENCY_KEY;
import static truelayer.java.http.interceptors.logging.HttpLogPrefix.INCOMING;
import static truelayer.java.http.interceptors.logging.HttpLogPrefix.OUTGOING;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.tinylog.TaggedLogger;
import org.tinylog.ThreadContext;
import truelayer.java.http.LoggerFactory;

public class HttpLoggingInterceptor implements Interceptor {
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
                "{} {} {} headers={}",
                OUTGOING,
                request.method(),
                request.url(),
                sensitiveHeaderGuard.getSanitizedHeaders(request.headers()));

        Response response = chain.proceed(request);
        logger.trace(
                "{} {} {} {} headers={}",
                INCOMING,
                response.code(),
                request.method(),
                request.url(),
                sensitiveHeaderGuard.getSanitizedHeaders(request.headers()));

        ThreadContext.clear();
        return response;
    }
}
