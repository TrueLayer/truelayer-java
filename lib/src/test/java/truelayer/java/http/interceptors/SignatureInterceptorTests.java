package truelayer.java.http.interceptors;

import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import truelayer.java.TestUtils;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static truelayer.java.Constants.HeaderNames.TL_SIGNATURE;

class SignatureInterceptorTests extends BaseInterceptorTests {

    private static final String A_PAYLOAD = "{\"foo\":\"bar\"}";

    @Override
    protected Interceptor getInterceptor() {
        return new SignatureInterceptor(TestUtils.getSigningOptions());
    }

    @Test
    @SneakyThrows
    @DisplayName("It should not add a Tl-Signature header on a GET request")
    public void shouldNotAddATlSignatureHeaderOnGet() {
        prepare(new Request.Builder().url("http://localhost").get().build());

        intercept();

        verifyThat(interceptedRequest -> {
            assertNull(interceptedRequest.header(TL_SIGNATURE));
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("It should add a Tl-Signature header on a GET request")
    public void shouldAddATlSignatureHeaderOnPost() {
        prepare(
                new Request.Builder().url("http://localhost")
                        .post(RequestBody.create(A_PAYLOAD.getBytes(StandardCharsets.UTF_8))).build()
        );

        intercept();

        verifyThat(request -> {
            assertFalse(request.header(TL_SIGNATURE).isEmpty());
        });
    }

    private void prepare(Request request) {
        chain = mock(Interceptor.Chain.class);
        when(chain.request()).thenReturn(request);
    }
}