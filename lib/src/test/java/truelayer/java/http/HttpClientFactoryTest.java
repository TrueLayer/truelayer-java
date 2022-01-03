package truelayer.java.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpClientFactoryTest {

    public static final String A_BASE_URL = "http://a-base-url.com/";

    @Test
    @DisplayName("It should reuse the same factory instance multiple times")
    public void testSingleton(){
        var sut1 = HttpClientFactory.getInstance();
        var sut2 = HttpClientFactory.getInstance();

        assertEquals(sut1, sut2);
    }

    @Test
    @DisplayName("It should build an HTTP client with the given base URL")
    public void testCreation(){
        var sut = HttpClientFactory.getInstance();

        var client = sut.create(A_BASE_URL);

        assertEquals(A_BASE_URL, client.baseUrl().toString());
    }
}