package truelayer.java.http.interceptors;

import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public abstract class BaseInterceptorTests {
    protected Interceptor.Chain chain;

    protected abstract Interceptor getInterceptor();

    @BeforeEach
    protected void buildRequest() {
        var request = new Request.Builder().url(HttpUrl.get("http://localhost")).build();
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
