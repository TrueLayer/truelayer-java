package truelayer.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.Builder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.Mockito;
import retrofit2.Response;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.entities.paymentdetail.AuthorizationRequired;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;
import truelayer.java.payments.entities.paymentmethod.BankTransfer;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static truelayer.java.Constants.HeaderNames.*;
import static truelayer.java.Utils.getObjectMapper;

public class TestUtils {

    private static final String KEYS_LOCATION = "src/test/resources/keys/";
    private static final String JSON_RESPONSES_LOCATION = "src/test/resources/__files/";

    public static ClientCredentials getClientCredentials() {
        return ClientCredentials.builder()
                .clientId("a-client-id")
                .clientSecret("a-secret").build();
    }

    @SneakyThrows
    public static SigningOptions getSigningOptions() {
        return SigningOptions.builder()
                .keyId("a-key-id")
                .privateKey(getPrivateKey())
                .build();
    }

    @SneakyThrows
    public static <T> ApiCall<T> stubApiResponse(Response<T> response) {
        var mockApiResponse = Mockito.mock(ApiCall.class);
        when(mockApiResponse.execute()).thenReturn(response);
        return mockApiResponse;
    }

    @SneakyThrows
    public static byte[] getPrivateKey() {
        return Files.readAllBytes(Path.of(new StringBuilder(KEYS_LOCATION).append("ec512-private-key.pem").toString()));
    }

    @SneakyThrows
    public static byte[] getPublicKey() {
        return Files.readAllBytes(Path.of(new StringBuilder(KEYS_LOCATION).append("ec512-public-key.pem").toString()));
    }

    public static ApiResponse<AccessToken> buildAccessToken() {
        var accessToken = new AccessToken(UUID.randomUUID().toString(), RandomUtils.nextInt(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        return ApiResponse.<AccessToken>builder()
                .data(accessToken)
                .build();
    }

    public static CreatePaymentResponse buildCreatePaymentResponse() {
        return new CreatePaymentResponse(
                UUID.randomUUID().toString(),
                new CreatePaymentResponse.User(UUID.randomUUID().toString()),
                UUID.randomUUID().toString()
        );
    }

    public static BasePaymentDetail buildGetPaymentByIdResponse() {
        return new AuthorizationRequired(
                UUID.randomUUID().toString(),
                101,
                "GBP",
                MerchantAccount.builder().name("whatever").build(),
                new BasePaymentDetail.User(
                        UUID.randomUUID().toString(),
                        "John Doe",
                        Optional.of("jdon@email.com"),
                        Optional.of("333221133")
                ),
                BankTransfer.builder().build(),
                new Date().toString());
    }

    public static ProblemDetails buildError() {
        return ProblemDetails
                .builder()
                .title("an-error")
                .build();
    }

    /**
     * Utility to assert that an API response is not an error.
     * It prints a meaningful message otherwise
     * @param apiResponse the api response to check
     */
    public static void assertNotError(ApiResponse apiResponse){
        assertFalse(apiResponse.isError(),
                String.format("request failed with error: %s", apiResponse.getError()));
    }

    @SneakyThrows
    public static <T> T deserializeJsonFileTo(String jsonFile, Class<T> type){
        return getObjectMapper().readValue(
                Files.readAllBytes(Path.of(new StringBuilder(JSON_RESPONSES_LOCATION).append(jsonFile).toString())),
                type);
    }

    @Builder
    public static class RequestStub {
        private static final String LIBRARY_INFO = "truelayer-java/DEVELOPMENT";
        private final static String UUID_REGEX_PATTERN = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";
        private final boolean withSignature;
        private final boolean withAuthorization;
        private String method;
        private UrlPattern path;
        private int status;
        private String bodyFile;

        public static class RequestStubBuilder {

            public RequestStubBuilder withAuthorization() {
                this.withAuthorization = true;
                return this;
            }

            public RequestStubBuilder withSignature() {
                this.withSignature = true;
                return this;
            }

            public StubMapping build() {
                var request = request(method.toUpperCase(), path)
                        .withHeader(IDEMPOTENCY_KEY, matching(UUID_REGEX_PATTERN))
                        .withHeader(USER_AGENT, matching(LIBRARY_INFO));

                if (withSignature) {
                    request.withHeader(TL_SIGNATURE, matching(".*"));
                }

                if (withAuthorization) {
                    request.withHeader(AUTHORIZATION, matching(".*"));
                }
                return stubFor(
                        request.willReturn(aResponse().withStatus(status).withBodyFile(bodyFile))
                );
            }
        }
    }
}
