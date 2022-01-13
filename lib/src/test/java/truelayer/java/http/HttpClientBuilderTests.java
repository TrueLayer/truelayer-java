package truelayer.java.http;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TrueLayerException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpClientBuilderTests {

    public static final String A_BASE_URL = "http://a-base-url.com/";

    @Test
    @DisplayName("It should build an HTTP client with the given base URL")
    public void testCreation(){
        var client = new HttpClientBuilder().baseUrl(A_BASE_URL)
                .build();

        assertEquals(A_BASE_URL, client.baseUrl().toString());
    }

    @Test
    @DisplayName("It should throw an exception if base url is not specified")
    public void testFailedCreation(){
        assertThrows(TrueLayerException.class, ()-> new HttpClientBuilder().build());
    }

    @Test
    @DisplayName("It should build an HTTP client with the given base URL and interceptors")
    public void testCreationWithInterceptors(){
        var dummyInterceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                return null;
            }
        };

        var client = new HttpClientBuilder().baseUrl(A_BASE_URL)
                .applicationInterceptors(List.of(dummyInterceptor))
                .networkInterceptors(List.of(dummyInterceptor))
                .build();

        assertEquals(A_BASE_URL, client.baseUrl().toString());
    }
}