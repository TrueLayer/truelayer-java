package com.truelayer.java.http.interceptors;

import static org.mockito.Mockito.*;

import com.truelayer.java.Constants;
import com.truelayer.java.entities.IdempotencyKey;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.apache.commons.lang3.ObjectUtils;
import org.mockito.ArgumentCaptor;

public abstract class BaseInterceptorTests {
    protected Interceptor.Chain chain;

    protected abstract Interceptor getInterceptor();

    protected void buildRequest() {
        buildRequest(null);
    }

    protected void buildRequest(IdempotencyKey idempotencyKey) {
        Request.Builder requestBuilder = new Request.Builder().url(HttpUrl.get("http://localhost"));

        if (ObjectUtils.isNotEmpty(idempotencyKey)) {
            requestBuilder.addHeader(Constants.HeaderNames.IDEMPOTENCY_KEY, idempotencyKey.getValue());
        }

        chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(requestBuilder.build());
    }

    @SneakyThrows
    protected void intercept() {
        getInterceptor().intercept(chain);
    }

    @SneakyThrows
    protected void verifyThat(Consumer<Request> consumer) {
        ArgumentCaptor<Request> actualRequest = ArgumentCaptor.forClass(Request.class);
        verify(chain).proceed(actualRequest.capture());
        consumer.accept(actualRequest.getValue());
    }
}
