package truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.*;
import static truelayer.java.TestUtils.*;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import truelayer.java.auth.entities.AccessToken;
import truelayer.java.http.entities.ApiResponse;
import truelayer.java.http.entities.ProblemDetails;
import truelayer.java.http.mappers.ErrorMapper;
import truelayer.java.payments.entities.*;

@WireMockTest
@Tag("integration")
@DisplayName("Auth integration tests")
public class AuthIntegrationTests extends IntegrationTests {

    @Test
    @DisplayName("It should return an error in case on an authorized error from the auth API.")
    @SneakyThrows
    public void shouldReturnErrorIfUnauthorized() {
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(400)
                .bodyFile("auth/400.invalid_client.json")
                .build();

        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList("payments"))
                .get();

        assertTrue(response.isError());
        ProblemDetails problemDetails = response.getError();
        assertEquals("invalid_client", problemDetails.getTitle());
        assertEquals(ErrorMapper.GENERIC_ERROR_TYPE, problemDetails.getType());
        assertEquals(400, problemDetails.getStatus());
        assertFalse(problemDetails.getTraceId().isEmpty());
    }

    @Test
    @DisplayName("It should create and return an access token")
    @SneakyThrows
    public void shouldReturnAnAccessToken() {
        String jsonResponseFile = "auth/200.access_token.json";
        RequestStub.New()
                .method("post")
                .path(urlPathEqualTo("/connect/token"))
                .status(200)
                .bodyFile(jsonResponseFile)
                .build();

        ApiResponse<AccessToken> response = tlClient.auth()
                .getOauthToken(Collections.singletonList("payments"))
                .get();

        assertNotError(response);
        AccessToken expected = deserializeJsonFileTo(jsonResponseFile, AccessToken.class);
        assertEquals(expected, response.getData());
    }
}
