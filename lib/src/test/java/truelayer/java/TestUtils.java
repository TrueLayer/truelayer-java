package truelayer.java;

import lombok.SneakyThrows;
import org.mockito.Mockito;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.when;

public class TestUtils {

    public static ClientCredentialsOptions getClientCredentialsOptions(){
        return ClientCredentialsOptions.builder()
                .clientId("a-client-id")
                .clientSecret("a-secret").build();
    }

    public static SigningOptions getSigningOptions(){
        return SigningOptions.builder()
                .keyId("a-key-id")
                .privateKey("a-private-key".getBytes()).build();
    }

    @SneakyThrows
    public static <T> Call<T> stubApiResponse(Response<T> response){
        var mockApiResponse = (Call<T>) Mockito.mock(Call.class);
        when(mockApiResponse.execute()).thenReturn(response);
        return mockApiResponse;
    }

}
