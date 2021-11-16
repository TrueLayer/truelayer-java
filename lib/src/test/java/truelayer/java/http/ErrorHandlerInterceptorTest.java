package truelayer.java.http;

import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import truelayer.java.TrueLayerException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorHandlerInterceptorTest {

    @SneakyThrows
    @Test
    @DisplayName("It should return a TrueLayerException instance")
    public void itShouldThrowAnException() {
        var requestChain = mock(Interceptor.Chain.class);
        when(requestChain.proceed(ArgumentMatchers.any())).thenThrow(new IOException("server error"));
        var sut = new ErrorHandlerInterceptor();

        var exception = Assertions.assertThrows(TrueLayerException.class,
                () -> sut.intercept(requestChain));

        assertNotNull(exception);
        Assertions.assertTrue(exception.getMessage().contains("server error"));
    }

    @SneakyThrows
    @Test
    @DisplayName("It should not throw any exception")
    public void itShouldNotThrowAnyException() {
        var requestChain = mock(Interceptor.Chain.class);
        when(requestChain.proceed(ArgumentMatchers.any())).thenReturn(new Response.Builder()
                .request(mock(Request.class))
                .protocol(Protocol.HTTP_2)
                .message("a message")
                .code(200)
                .build());
        var sut = new ErrorHandlerInterceptor();

        var response = sut.intercept(requestChain);

        assertTrue(response.isSuccessful());
    }
}
