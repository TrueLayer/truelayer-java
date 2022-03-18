package com.truelayer.java.http.interceptors.logging;

import static com.truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static com.truelayer.java.Constants.HeaderNames.COOKIE;
import static com.truelayer.java.TestUtils.JSON_RESPONSES_LOCATION;
import static org.mockito.Mockito.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import okhttp3.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class HttpLoggingInterceptorTests {

    @SneakyThrows
    @Test
    @DisplayName("It should log request and response")
    public void shouldLogRequestAndResponse() {
        int responseCode = 200;
        HttpUrl url = HttpUrl.get("http://localhost");
        Request request = new Request.Builder()
                .url(url)
                .header(AUTHORIZATION, "a-sensitive-token")
                .build();
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        ResponseBody jsonBody = ResponseBody.create(
                MediaType.get("application/json"),
                Files.readAllBytes(Paths.get(JSON_RESPONSES_LOCATION + "/auth/200.access_token.json")));
        Response response = new okhttp3.Response.Builder()
                .protocol(Protocol.HTTP_2)
                .request(request)
                .message("")
                .body(jsonBody)
                .code(responseCode)
                .header(COOKIE, "a-cookie")
                .build();
        when(chain.proceed(request)).thenReturn(response);

        DefaultLogConsumer logConsumer = Mockito.mock(DefaultLogConsumer.class);
        SensitiveHeaderGuard sensitiveHeaderGuard = Mockito.mock(SensitiveHeaderGuard.class);
        HttpLoggingInterceptor sut = new HttpLoggingInterceptor(logConsumer, sensitiveHeaderGuard);

        sut.intercept(chain);

        InOrder interactions = inOrder(logConsumer, chain);
        interactions.verify(chain).request();
        interactions.verify(logConsumer).accept(anyString());
        interactions.verify(chain).proceed(request);
        interactions.verify(logConsumer).accept(anyString());
    }
}
