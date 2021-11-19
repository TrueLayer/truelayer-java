package truelayer.java;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.Mockito;
import retrofit2.Call;
import retrofit2.Response;
import truelayer.java.auth.entities.AccessToken;
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
    public static <T> Call<T> stubApiResponse(Response<T> response) {
        var mockApiResponse = (Call<T>) Mockito.mock(Call.class);
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

    public static AccessToken buildAccessToken() {
        return AccessToken.builder()
                .accessToken(UUID.randomUUID().toString())
                .expiresIn(RandomUtils.nextInt())
                .tokenType(UUID.randomUUID().toString())
                .build();
    }

    public static Payment buildPayment(){
        return Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .resourceToken(UUID.randomUUID().toString())
                .status(UUID.randomUUID().toString())
                .build();
    }

}
