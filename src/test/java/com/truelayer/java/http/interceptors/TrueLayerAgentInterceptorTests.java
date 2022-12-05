package com.truelayer.java.http.interceptors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.truelayer.java.Constants;
import com.truelayer.java.TestUtils;
import okhttp3.Interceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TrueLayerAgentInterceptorTests extends BaseInterceptorTests {

    @Override
    protected Interceptor getInterceptor() {
        return new TrueLayerAgentInterceptor(TestUtils.getVersionInfo());
    }

    @BeforeEach
    public void prepareTest() {
        buildRequest();
    }

    @Test
    @DisplayName("It should add a User-Agent header containing the library name and version")
    public void shouldAddUserAgentHeader() {
        intercept();

        String javaVersionIdentifier =
                String.format("(Java/%s)", TestUtils.getVersionInfo().javaVersion());

        verifyThat(request -> assertEquals(
                String.format("%s/%s %s", TestUtils.LIBRARY_NAME, TestUtils.LIBRARY_VERSION, javaVersionIdentifier),
                request.header(Constants.HeaderNames.TL_AGENT)));
    }
}
