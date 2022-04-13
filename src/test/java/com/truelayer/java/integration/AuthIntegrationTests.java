package com.truelayer.java.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.truelayer.java.TestUtils.assertNotError;
import static org.junit.jupiter.api.Assertions.*;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.truelayer.java.TestUtils;
import com.truelayer.java.TestUtils.RequestStub;
import com.truelayer.java.auth.entities.AccessToken;
import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.http.entities.ProblemDetails;
import com.truelayer.java.http.mappers.ErrorMapper;
import java.util.Collections;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
        Assertions.assertEquals(ErrorMapper.GENERIC_ERROR_TYPE, problemDetails.getType());
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
        AccessToken expected = TestUtils.deserializeJsonFileTo(jsonResponseFile, AccessToken.class);
        assertEquals(expected, response.getData());
    }
}
