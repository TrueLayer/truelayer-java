package truelayer.java.http.interceptors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static truelayer.java.common.Constants.HeaderNames.IDEMPOTENCY_KEY;

import java.util.regex.Pattern;
import okhttp3.Interceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IdempotencyKeyInterceptorTests extends BaseInterceptorTests {

    private static final Pattern UUID_REGEX_PATTERN =
            Pattern.compile("^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$");

    @Override
    protected Interceptor getInterceptor() {
        return new IdempotencyKeyInterceptor();
    }

    @BeforeEach
    public void prepareTest() {
        buildRequest();
    }

    @Test
    @DisplayName("It should add an Idempotency-Key header with a UUID to the original request")
    public void shouldAddAnIdempotencyKeyHeader() {
        intercept();

        verifyThat(request -> {
            assertTrue(
                    UUID_REGEX_PATTERN.matcher(request.header(IDEMPOTENCY_KEY)).matches());
        });
    }
}
