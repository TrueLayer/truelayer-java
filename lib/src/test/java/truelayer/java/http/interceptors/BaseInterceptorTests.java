package truelayer.java.http.interceptors;

import static org.mockito.Mockito.*;

import java.util.function.Consumer;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import org.mockito.ArgumentCaptor;

public abstract class BaseInterceptorTests {
    protected Interceptor.Chain chain;

    protected abstract Interceptor getInterceptor();

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
