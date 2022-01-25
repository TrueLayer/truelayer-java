package truelayer.java.http.interceptors;

import static truelayer.java.common.Constants.HeaderNames.IDEMPOTENCY_KEY;

import java.io.IOException;
import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class IdempotencyKeyInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header(IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .build();

        return chain.proceed(newRequest);
    }
}
