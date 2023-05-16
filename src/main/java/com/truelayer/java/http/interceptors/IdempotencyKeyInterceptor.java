package com.truelayer.java.http.interceptors;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

import com.truelayer.java.Constants;
import com.truelayer.java.entities.IdempotencyKey;
import java.io.IOException;
import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class IdempotencyKeyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String idempotencyKey = getOrGenerateIdempotencyKey(request);

        Request newRequest = request.newBuilder()
                .header(Constants.HeaderNames.IDEMPOTENCY_KEY, idempotencyKey)
                .build();

        return chain.proceed(newRequest);
    }

    private String getOrGenerateIdempotencyKey(Request request) {
        IdempotencyKey key = request.tag(IdempotencyKey.class);
        return key != null && isNotEmpty(key.getValue())
                ? key.getValue()
                : UUID.randomUUID().toString();
    }
}
