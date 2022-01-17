package truelayer.java.http.interceptors;

import lombok.SneakyThrows;
import okhttp3.Interceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static truelayer.java.Constants.HeaderNames.USER_AGENT;

class UserAgentInterceptorTests extends BaseInterceptorTests {
    public static final String LIBRARY_NAME = "truelayer-java";
    public static final String LIBRARY_VERSION = "DEVELOPMENT";

    @Override
    protected Interceptor getInterceptor() {
        return new UserAgentInterceptor(LIBRARY_NAME, LIBRARY_VERSION);
    }

    @BeforeEach
    public void prepareTest(){
        buildRequest();
    }

    @Test
    @DisplayName("It should add a User-Agent header containing the library name and version")
    public void shouldAddUserAgentHeader() {
        intercept();

        verifyThat(request -> {
            assertEquals(String.format("%s/%s", LIBRARY_NAME, LIBRARY_VERSION), request.header(USER_AGENT));
        });
    }
}