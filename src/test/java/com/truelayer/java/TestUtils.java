package com.truelayer.java;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.truelayer.java.Constants.HeaderNames.*;
import static com.truelayer.java.Utils.getObjectMapper;
import static org.apache.commons.lang3.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.versioninfo.VersionInfo;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.UUID;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;

public class TestUtils {

    private static final OkHttpClient HTTP_CLIENT_INSTANCE = new OkHttpClient.Builder()
            .followRedirects(false)
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private static final String KEYS_LOCATION = "src/test/resources/keys/";
    public static final String JSON_RESPONSES_LOCATION = "src/test/resources/__files/";

    public static final String LIBRARY_NAME = "truelayer-java";
    public static final String LIBRARY_VERSION = "DEVELOPMENT";

    public static final URI A_HPP_ENDPOINT = URI.create("https://hpp.truelayer.com");

    public static ClientCredentials getClientCredentials() {
        return ClientCredentials.builder()
                .clientId("a-client-id")
                .clientSecret("a-secret")
                .build();
    }

    public static VersionInfo getVersionInfo() {
        return VersionInfo.builder()
                .libraryName(LIBRARY_NAME)
                .libraryVersion(LIBRARY_VERSION)
                .build();
    }

    public static Environment getTestEnvironment(URI endpointUrl) {
        return Environment.custom(endpointUrl, endpointUrl, endpointUrl);
    }

    private TestUtils() {}

    @SneakyThrows
    public static SigningOptions getSigningOptions() {
        return SigningOptions.builder()
                .keyId("a-key-id")
                .privateKey(getPrivateKey())
                .build();
    }

    @SneakyThrows
    public static byte[] getPrivateKey() {
        return Files.readAllBytes(Paths.get(KEYS_LOCATION + "ec512-private-key.pem"));
    }

    public static ApiResponse<AccessToken> buildAccessToken() {
        AccessToken accessToken = new AccessToken(
                UUID.randomUUID().toString(),
                3600,
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
        return ApiResponse.<AccessToken>builder().data(accessToken).build();
    }

    /**
     * Utility to assert that an API response is not an error.
     * It prints a meaningful message otherwise
     *
     * @param apiResponse the api response to check
     */
    public static <T> void assertNotError(ApiResponse<T> apiResponse) {
        assertFalse(apiResponse.isError(), String.format("request failed with error: %s", apiResponse.getError()));
    }

    @SneakyThrows
    public static <T> T deserializeJsonFileTo(String jsonFile, Class<T> type) {
        return getObjectMapper().readValue(Files.readAllBytes(Paths.get(JSON_RESPONSES_LOCATION + jsonFile)), type);
    }

    public static class RequestStub {
        private static final String LIBRARY_INFO = "truelayer-java\\/.+";
        private static final String UUID_REGEX_PATTERN = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";

        private boolean withSignature;
        private boolean withAuthorization;
        private boolean withIdempotencyKey;
        private String method;
        private UrlPattern path;
        private int status;
        private String bodyFile;
        private Integer delayMilliseconds;

        private RequestStub() {}

        public static RequestStub New() {
            return new RequestStub();
        }

        public RequestStub method(String method) {
            this.method = method;
            return this;
        }

        public RequestStub path(UrlPattern path) {
            this.path = path;
            return this;
        }

        public RequestStub status(int status) {
            this.status = status;
            return this;
        }

        public RequestStub bodyFile(String bodyFile) {
            this.bodyFile = bodyFile;
            return this;
        }

        public RequestStub withAuthorization() {
            this.withAuthorization = true;
            return this;
        }

        public RequestStub withIdempotencyKey() {
            this.withIdempotencyKey = true;
            return this;
        }

        public RequestStub withSignature() {
            this.withSignature = true;
            return this;
        }

        public RequestStub delayMs(int delayMilliseconds) {
            this.delayMilliseconds = delayMilliseconds;
            return this;
        }

        public StubMapping build() {
            MappingBuilder request = request(method.toUpperCase(), path).withHeader(USER_AGENT, matching(LIBRARY_INFO));

            if (withSignature) {
                request.withHeader(TL_SIGNATURE, matching(".*"));
            }

            if (withAuthorization) {
                request.withHeader(AUTHORIZATION, matching(".*"));
            }

            if (withIdempotencyKey) {
                request.withHeader(IDEMPOTENCY_KEY, matching(UUID_REGEX_PATTERN));
            }

            ResponseDefinitionBuilder response = aResponse()
                    .withHeader(TL_CORRELATION_ID, UUID.randomUUID().toString())
                    .withStatus(status);

            if (isNotEmpty(delayMilliseconds)) {
                response.withFixedDelay(delayMilliseconds);
            }

            if (!isEmpty(bodyFile)) {
                response.withBodyFile(bodyFile);
            }

            return stubFor(request.willReturn(response));
        }
    }

    public static OkHttpClient getHttpClientInstance() {
        return HTTP_CLIENT_INSTANCE;
    }
}
