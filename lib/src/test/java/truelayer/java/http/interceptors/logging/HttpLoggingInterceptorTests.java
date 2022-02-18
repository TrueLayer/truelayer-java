package truelayer.java.http.interceptors.logging;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static truelayer.java.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.Constants.HeaderNames.COOKIE;
import static truelayer.java.TestUtils.JSON_RESPONSES_LOCATION;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;
import okhttp3.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.tinylog.TaggedLogger;

class HttpLoggingInterceptorTests {

    @SneakyThrows
    @Test
    @DisplayName("it should log request and response")
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
        TaggedLogger logger = Mockito.mock(TaggedLogger.class);
        SensitiveHeaderGuard sensitiveHeaderGuard = Mockito.mock(SensitiveHeaderGuard.class);
        HttpLoggingInterceptor sut = new HttpLoggingInterceptor(logger, sensitiveHeaderGuard);

        sut.intercept(chain);

        InOrder interactions = inOrder(logger, chain);
        interactions.verify(chain).request();
        interactions.verify(logger).trace(startsWith("-->"), eq("GET"), eq(url), anyList());
        interactions.verify(chain).proceed(request);
        interactions.verify(logger).trace(startsWith("<--"), eq(responseCode), eq(url), anyList(), isNull());
    }
}
