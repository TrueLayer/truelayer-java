package truelayer.java.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class IdempotencyKeyInterceptor implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var request = chain.request();
        var newRequest = request.newBuilder()
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .build();

        return chain.proceed(newRequest);
    }
}
