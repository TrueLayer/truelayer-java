package truelayer.java;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static truelayer.java.common.Constants.HeaderNames.*;
import static truelayer.java.common.Utils.getObjectMapper;

import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.configuration.Configuration;
import truelayer.java.configuration.Configuration.Endpoint;
import truelayer.java.configuration.Configuration.Payments;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.entities.CreatePaymentResponse;
import truelayer.java.payments.entities.User;
import truelayer.java.payments.entities.beneficiary.MerchantAccount;
import truelayer.java.payments.entities.paymentdetail.AuthorizationRequiredPaymentDetail;
import truelayer.java.payments.entities.paymentdetail.BasePaymentDetail;
import truelayer.java.payments.entities.paymentmethod.BankTransfer;

public class TestUtils {

    private static final String KEYS_LOCATION = "src/test/resources/keys/";
    public static final String JSON_RESPONSES_LOCATION = "src/test/resources/__files/";

    public static final String LIBRARY_NAME = "truelayer-java";
    public static final String LIBRARY_VERSION = "DEVELOPMENT";

    public static final String A_HPP_ENDPOINT = "https://hpp.truelayer.com";
    public static final String AN_AUTH_ENDPOINT = "https://auth.truelayer.com";
    public static final String A_PAYMENTS_ENDPOINT = "https://pay.truelayer.com";

    public static final List A_PAYMENT_SCOPE = Collections.singletonList("https://pay.truelayer.com");

    public static ClientCredentials getClientCredentials() {
        return ClientCredentials.builder()
                .clientId("a-client-id")
                .clientSecret("a-secret")
                .build();
    }

    public static Configuration getConfiguration() {
        return Configuration.builder()
                .versionInfo(Configuration.VersionInfo.builder()
                        .libraryName(LIBRARY_NAME)
                        .libraryVersion(LIBRARY_VERSION)
                        .build())
                .hostedPaymentPage(new Endpoint(A_HPP_ENDPOINT))
                .authentication(new Endpoint(AN_AUTH_ENDPOINT))
                .payments(Payments.builder()
                        .scopes(A_PAYMENT_SCOPE)
                        .endpointUrl(A_PAYMENTS_ENDPOINT)
                        .build())
                .build();
    }

    @SneakyThrows
    public static SigningOptions getSigningOptions() {
        return SigningOptions.builder()
                .keyId("a-key-id")
                .privateKey(getPrivateKey())
                .build();
    }

    @SneakyThrows
    public static byte[] getPrivateKey() {
        return Files.readAllBytes(Paths.get(
                new StringBuilder(KEYS_LOCATION).append("ec512-private-key.pem").toString()));
    }

    public static ApiResponse<AccessToken> buildAccessToken() {
        AccessToken accessToken = new AccessToken(
                UUID.randomUUID().toString(),
                RandomUtils.nextInt(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString());
        return ApiResponse.<AccessToken>builder().data(accessToken).build();
    }

    public static CreatePaymentResponse buildCreatePaymentResponse() {
        return new CreatePaymentResponse(
                UUID.randomUUID().toString(),
                User.builder().id(UUID.randomUUID().toString()).name("John Doe").build(),
                UUID.randomUUID().toString());
    }

    public static BasePaymentDetail buildGetPaymentByIdResponse() {
        AuthorizationRequiredPaymentDetail payment = new AuthorizationRequiredPaymentDetail();
        payment.setId(UUID.randomUUID().toString());
        payment.setAmountInMinor(101);
        payment.setCurrency("GBP");
        payment.setBeneficiary(MerchantAccount.builder().name("whatever").build());
        payment.setUser(User.builder()
                .id(UUID.randomUUID().toString())
                .name("John Doe")
                .email("jdon@email.com")
                .phone("333221133")
                .build());
        payment.setPaymentMethod(BankTransfer.builder().build());
        payment.setCreatedAt(new Date());
        return payment;
    }

    public static ProblemDetails buildError() {
        return ProblemDetails.builder().title("an-error").build();
    }

    /**
     * Utility to assert that an API response is not an error.
     * It prints a meaningful message otherwise
     *
     * @param apiResponse the api response to check
     */
    public static void assertNotError(ApiResponse apiResponse) {
        assertFalse(apiResponse.isError(), String.format("request failed with error: %s", apiResponse.getError()));
    }

    @SneakyThrows
    public static <T> T deserializeJsonFileTo(String jsonFile, Class<T> type) {
        return getObjectMapper()
                .readValue(
                        Files.readAllBytes(Paths.get(new StringBuilder(JSON_RESPONSES_LOCATION)
                                .append(jsonFile)
                                .toString())),
                        type);
    }

    @Builder
    public static class RequestStub {
        private static final String LIBRARY_INFO = "truelayer-java/DEVELOPMENT";
        private static final String UUID_REGEX_PATTERN = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";
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
                MappingBuilder request = request(method.toUpperCase(), path)
                        .withHeader(IDEMPOTENCY_KEY, matching(UUID_REGEX_PATTERN))
                        .withHeader(USER_AGENT, matching(LIBRARY_INFO));

                if (withSignature) {
                    request.withHeader(TL_SIGNATURE, matching(".*"));
                }

                if (withAuthorization) {
                    request.withHeader(AUTHORIZATION, matching(".*"));
                }
                return stubFor(request.willReturn(aResponse().withStatus(status).withBodyFile(bodyFile)));
            }
        }
    }
}
