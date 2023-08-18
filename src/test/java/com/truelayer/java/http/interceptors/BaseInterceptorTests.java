package com.truelayer.java.http.interceptors;

import static org.mockito.Mockito.*;

import com.truelayer.java.Constants;
import com.truelayer.java.entities.RequestScopes;
import java.util.function.Consumer;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.mockito.ArgumentCaptor;

public abstract class BaseInterceptorTests {
    protected final RequestScopes SCOPES =
            RequestScopes.builder().scope(Constants.Scopes.PAYMENTS).build();

    protected Interceptor.Chain chain;

    protected abstract Interceptor getInterceptor();

    protected void arrangeRequest() {
        Request request = new Request.Builder()
                .url(HttpUrl.get("http://localhost"))
                .tag(RequestScopes.class, SCOPES)
                .build();
        arrangeRequest(request);
    }

    protected void arrangeRequest(Request request) {
        chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
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
