package truelayer.java;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.Mockito;
import retrofit2.Response;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.adapters.ApiCall;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.payments.entities.BankTransfer;
import truelayer.java.payments.entities.MerchantAccount;
import truelayer.java.payments.entities.Payment;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class TestUtils {

    private static final String KEYS_LOCATION = "src/test/resources/keys/";

    public static ClientCredentialsOptions getClientCredentialsOptions() {
        return ClientCredentialsOptions.builder()
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

    public static Payment buildPayment() {
        return new Payment(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                new Payment.User(UUID.randomUUID().toString())
        );
    }

    public static ProblemDetails buildError() {
        return ProblemDetails
                .builder()
                .title("an-error")
                .build();
    }

}
