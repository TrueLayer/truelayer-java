package truelayer.java.http.interceptors.logging;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static truelayer.java.TestUtils.JSON_RESPONSES_LOCATION;
import static truelayer.java.common.Constants.HeaderNames.AUTHORIZATION;
import static truelayer.java.common.Constants.HeaderNames.COOKIE;

import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import okhttp3.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tinylog.TaggedLogger;

class HttpLoggingInterceptorTests {

    @SneakyThrows
    @Test
    @DisplayName("it should log request and response")
    public void shouldLogRequestAndResponse() {
        int responseCode = 200;
        var url = HttpUrl.get("http://localhost");
        var request = new Request.Builder()
                .url(url)
                .header(AUTHORIZATION, "a-sensitive-token")
                .build();
        var chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
        var jsonBody = ResponseBody.create(
                MediaType.get("application/json"),
                Files.readString(Path.of(new StringBuilder(JSON_RESPONSES_LOCATION)
                        .append("/auth/200.access_token.json")
                        .toString())));
        var response = new okhttp3.Response.Builder()
                .protocol(Protocol.HTTP_2)
                .request(request)
                .message("")
                .body(jsonBody)
                .code(responseCode)
                .header(COOKIE, "a-cookie")
                .build();
        when(chain.proceed(request)).thenReturn(response);
        var logger = Mockito.mock(TaggedLogger.class);
        var sensitiveHeaderGuard = Mockito.mock(SensitiveHeaderGuard.class);
        var sut = new HttpLoggingInterceptor(logger, sensitiveHeaderGuard);

        sut.intercept(chain);

        var interactions = inOrder(logger, chain);
        interactions.verify(chain).request();
        interactions.verify(logger).debug(startsWith("-->"), eq("GET"), eq(url), anyList());
        interactions.verify(chain).proceed(request);
        interactions.verify(logger).debug(startsWith("<--"), eq(responseCode), eq(url), anyList(), isNull());
    }
}
