package com.truelayer.java.http.interceptors;

import com.truelayer.java.Constants;
import java.io.IOException;
import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class IdempotencyKeyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder()
                .header(Constants.HeaderNames.IDEMPOTENCY_KEY, UUID.randomUUID().toString())
                .build();

        return chain.proceed(newRequest);
    }
}
