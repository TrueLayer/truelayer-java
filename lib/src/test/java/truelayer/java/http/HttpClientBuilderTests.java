package truelayer.java.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import okhttp3.Interceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import truelayer.java.TrueLayerException;

class HttpClientBuilderTests {

    public static final String A_BASE_URL = "http://a-base-url.com/";

    @Test
    @DisplayName("It should build an HTTP client with the given base URL")
    public void testCreation() {
        Retrofit client = new HttpClientBuilder().baseUrl(A_BASE_URL).build();

        assertEquals(A_BASE_URL, client.baseUrl().toString());
    }

    @Test
    @DisplayName("It should throw an exception if base url is not specified")
    public void testFailedCreation() {
        assertThrows(TrueLayerException.class, () -> new HttpClientBuilder().build());
    }

    @Test
    @DisplayName("It should build an HTTP client with the given base URL and interceptors")
    public void testCreationWithInterceptors() {
        Interceptor dummyInterceptor = chain -> null;

        Retrofit client = new HttpClientBuilder()
                .baseUrl(A_BASE_URL)
                .applicationInterceptors(Collections.singletonList(dummyInterceptor))
                .networkInterceptors(Collections.singletonList(dummyInterceptor))
                .build();

        assertEquals(A_BASE_URL, client.baseUrl().toString());
    }
}
